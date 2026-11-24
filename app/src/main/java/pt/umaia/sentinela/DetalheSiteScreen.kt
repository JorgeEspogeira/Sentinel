package pt.umaia.sentinela

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Ecrã com os detalhes de um site
@Composable
fun DetalheSiteScreen(
    site: Site,                // site a mostrar
    onVoltar: () -> Unit       // chamada ao carregar em "Voltar"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(site.nome, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(site.url)
        Text(if (site.online) "Online (${site.tempoMs} ms)" else "Offline")

        Button(onClick = onVoltar) {
            Text("Voltar")
        }

        Text("Últimas medições:", fontWeight = FontWeight.Bold)

        // Uma linha por medição, da mais recente para a mais antiga
        // (0 = nessa altura o site estava offline)
        LazyColumn {
            items(site.historico.reversed()) { tempo ->
                Text(if (tempo == 0) "Offline" else "$tempo ms")
            }
        }
    }
}