package edu.utsa.cs3773.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3773.bookworm.databinding.ActivityMainBinding;
import edu.utsa.cs3773.bookworm.model.User;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Set up NavController for fragment navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        NavController navController;
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Configure the AppBar with the navigation destinations
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_explore, R.id.nav_library // Set up bottom navigation root destinations here
            ).build();

            // Setup the action bar with navController and appBarConfiguration
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            // Set up BottomNavigationView with NavController
            BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavigation;
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Listener for BottomNavigationView
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_explore) {
                    navController.navigate(R.id.nav_explore);
                    return true;
                } else if (id == R.id.nav_library) {
                    navController.navigate(R.id.nav_library);
                    return true;
                }
                return false;
            });
        } else {
            Log.e("MainActivity", "NavHostFragment not found in fragmentContainer.");
            finish();
            return;
        }

        loggedInUser = new User(0, "username", "password", "email");    //set loggedInUser based on database info
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainer);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_options, menu); // Inflate the 3-dot overflow menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainer);
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            navController.navigate(R.id.nav_search);
        } else if (id == R.id.nav_settings) {
            Bundle args = new Bundle();
            args.putBoolean("optionsVisible", ((Toolbar)findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_search).isVisible());    //must pass toolbar option visibility as argument when navigating to settings page
            args.putInt("bottomNavigationVisibility", findViewById(R.id.bottom_navigation).getVisibility());    //must pass bottom navigation visibility as argument when navigating to settings page
            navController.navigate(R.id.nav_settings, args);
        }  else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logging out... ", Toast.LENGTH_SHORT).show();
            //terminate session
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
