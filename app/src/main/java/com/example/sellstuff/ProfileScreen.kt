import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.example.sellstuff.AuthViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(authViewModel: AuthViewModel) {
    val user = authViewModel.user.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Image and Name Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(40.dp)  // Adjust size as needed
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(" ${user?.email ?: ""}")
            }
            Button(
                onClick = { authViewModel.logout() },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(16.dp)).background(Color(0XF9F9F9)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Blue,
                    contentColor = Color.White)
            ) {
                Text(text = "Log Out")
            }

        }

        // Menu Items
        Divider()
        MenuItem(icon = Icons.Default.Info, text = "About us")
        Divider()
        MenuItem(icon = Icons.Default.Share, text = "Share")
        Divider()
        MenuItem(icon = Icons.Default.Email, text = "Contact")
        Divider()

        // Delete Account Section
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Icon",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Delete your account", color = Color.Gray)
        }
    }


}

@Composable
fun MenuItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 18.sp)
    }
}



@Composable
fun ProfileScreenPreview() {

        ProfileScreen(authViewModel = AuthViewModel())
    }
         @Preview(showBackground = true)
         @Composable
         fun AboutUsScreenPreview() {
             AboutUsScreen()
         }

@Composable
fun AboutUsScreen() {
    TODO("Not yet implemented")
}





