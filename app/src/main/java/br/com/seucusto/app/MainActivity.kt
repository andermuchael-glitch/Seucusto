package br.com.seucusto.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.seucusto.app.ui.SeucustoApp
import br.com.seucusto.app.ui.theme.SeucustoTheme
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{SeucustoTheme{SeucustoApp()}}}}
