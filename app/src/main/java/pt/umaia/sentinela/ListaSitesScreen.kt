package pt.umaia.sentinela

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Dados de um site
data class Site(
    val nome: String,
    val url: String,
    val online: Boolean,
    val tempoMs: Int = 0,                      // tempo de resposta em milissegundos
    val historico: List<Int> = emptyList()     // tempos das últimas medições (0 = estava offline)
)

// Ecrã com a lista de sites
@Composable
fun ListaSitesScreen(
    sites: List<Site>,           // sites a mostrar
    onAdicionar: () -> Unit,     // chamada ao carregar em "+ Adicionar"
    onApagar: (Site) -> Unit,    // chamada ao carregar em "Apagar", com o site escolhido
    onAbrir: (Site) -> Unit,     // chamada ao carregar num site, para ver o detalhe
    onSair: () -> Unit           // chamada ao carregar em "Sair"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sites", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onSair) {
                    Text("Sair")
                }
                Button(onClick = onAdicionar) {
                    Text("+ Adicionar")
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(sites) { site ->
                SiteItem(site, onApagar, onAbrir)
            }
        }
    }
}

// Cartão de um site
@Composable
fun SiteItem(site: Site, onApagar: (Site) -> Unit, onAbrir: (Site) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAbrir(site) }   // carregar no cartão abre o detalhe
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(site.nome, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(site.url)
            Text(if (site.online) "Online" else "Offline")

            // Botão simples (sem fundo) que avisa qual o site a apagar
            TextButton(onClick = { onApagar(site) }) {
                Text("Apagar")
            }
        }
    }
}