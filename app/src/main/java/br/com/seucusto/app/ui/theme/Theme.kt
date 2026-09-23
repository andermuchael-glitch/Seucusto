package br.com.seucusto.app.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val Scheme=lightColorScheme(primary=Color(0xFF2E7D32),secondary=Color(0xFFF57C00),tertiary=Color(0xFFF57C00),background=Color.White,surface=Color.White)
@Composable fun SeucustoTheme(content:@Composable()->Unit)=MaterialTheme(colorScheme=Scheme,content=content)
