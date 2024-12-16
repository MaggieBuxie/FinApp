import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.finapp.navigation.ROUTE_ADD_CLIENT
import com.example.finapp.navigation.ROUTE_ADMIN_SETTINGS
import com.example.finapp.navigation.ROUTE_SERVICES
import com.example.finapp.navigation.ROUTE_UPDATE_CLIENT
import com.example.finapp.navigation.ROUTE_VIEW_CLIENTS

object Routes {
    const val ROUTE_ADMIN_HOME = "admin_home"
    const val ROUTE_ADMIN_SETTINGS = "admin_settings"
    const val ROUTE_ADD_CLIENT = "add_client"
    const val ROUTE_VIEW_CLIENTS = "view_clients"
    const val ROUTE_UPDATE_CLIENT = "update_client"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Home", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome, Admin!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            AdminFeatureButton("Go to Admin Settings", MaterialTheme.colorScheme.secondary) {

                 navController.navigate(ROUTE_ADMIN_SETTINGS)             }

            AdminFeatureButton("Add Client", MaterialTheme.colorScheme.tertiary) {

                navController.navigate(ROUTE_ADD_CLIENT)
            }

            AdminFeatureButton("View Clients", MaterialTheme.colorScheme.primaryContainer) {

                navController.navigate(ROUTE_VIEW_CLIENTS)
            }

            AdminFeatureButton("Update Client", MaterialTheme.colorScheme.error) {

                navController.navigate(ROUTE_UPDATE_CLIENT)
            }
        }
    }
}

@Composable
fun AdminFeatureButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = text, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Settings Options",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text("1. Update Admin Profile", fontSize = 16.sp)
            Text("2. Manage Permissions", fontSize = 16.sp)
            Text("3. System Logs", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Admin Home", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminHomePreview() {
    val navController = rememberNavController()
    MaterialTheme {
        AdminHomeScreen(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun AdminSettingsPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        AdminSettingsScreen(navController = navController)
    }
}
