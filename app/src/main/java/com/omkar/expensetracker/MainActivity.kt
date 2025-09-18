package com.omkar.expensetracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omkar.expensetracker.expenseentry.ExpenseEntryScreen
import com.omkar.expensetracker.expenselist.ExpenseListScreen
import com.omkar.expensetracker.expenselist.ExpenseListViewModel
import com.omkar.expensetracker.expensereport.ExpenseReportScreen
import com.omkar.expensetracker.expensereport.ExpenseReportViewModel
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme
import com.omkar.expensetracker.utils.generateExpenseReportPdfVisual
import com.omkar.expensetracker.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTopBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    expenseReportViewModel: ExpenseReportViewModel,
    context: Context
) {
    fun sharePdfFile(context: Context) {
        val pdfFile = context.generateExpenseReportPdfVisual(
            dailyTotals = expenseReportViewModel.dailyTotals.value,
            categoryTotals = expenseReportViewModel.categoryTotals.value
        )
        val uri = FileProvider.getUriForFile(
            context, "${context.packageName}.provider", pdfFile
        )
        context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }, "Share Expense Report"))
    }

    when (currentDestination?.route) {
        Screen.List.route -> {
            TopAppBar(title = { Text("DashBoard") })
        }

        Screen.Entry.route -> {
            CenterAlignedTopAppBar(title = { Text("Add Expense") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
        }

        Screen.Report.route -> {
            TopAppBar(title = { Text("Expense Report") }, actions = {
                IconButton(onClick = {
                    context.showToast("Exporting Report...")
                    sharePdfFile(context)
                }) {
                    Icon(Icons.Default.Share, contentDescription = "Export")
                }
            })
        }
    }
}

@Composable
fun ExpenseBottomBar(
    navController: NavHostController, currentDestination: NavDestination?, items: List<Screen>
) {
    if (currentDestination?.route != Screen.Entry.route) {
        NavigationBar {
            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        if (screen is Screen.List) {
                            Icon(Icons.Default.List, contentDescription = null)
                        } else {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        }
                    },
                    label = { Text(screen.title) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }
}

@Composable
fun ExpenseFab(navController: NavHostController, currentDestination: NavDestination?) {
    if (currentDestination?.route == Screen.List.route) {
        FloatingActionButton(onClick = {
            navController.navigate(Screen.Entry.route)
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add Expense")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerApp(
    viewModel: ExpenseListViewModel = hiltViewModel(),
    expenseReportViewModel: ExpenseReportViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val items = listOf(Screen.List, Screen.Report)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val displayItem by viewModel.displayItem.collectAsStateWithLifecycle()
    val totalAmount by viewModel.totalSpent.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val currentGroupingMode by viewModel.groupingMode.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ExpenseTopBar(navController, currentDestination, expenseReportViewModel, context)
        },
        bottomBar = {
            ExpenseBottomBar(navController, currentDestination, items)
        },
        floatingActionButton = { ExpenseFab(navController, currentDestination) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.List.route,
            modifier = Modifier.padding(innerPadding),
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }) {
            composable(Screen.List.route) {
                ExpenseListScreen(
                    expenses = expenses,
                    selectedDate = selectedDate,
                    totalAmount = totalAmount,
                    onDateChange = { viewModel.changeDate(it) },
                    currentGroupingMode = currentGroupingMode,
                    displayedItems = displayItem,
                    onToggleGroupingMode = {
                        viewModel.toggleGroupingMode()
                    })
            }
            composable(Screen.Report.route) {
                ExpenseReportScreen(expenseReportViewModel)
            }

            composable(Screen.Entry.route) {
                ExpenseEntryScreen(navController)
            }
        }
    }
}
