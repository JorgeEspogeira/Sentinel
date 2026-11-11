package pt.umaia.sentinela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import pt.umaia.sentinela.ui.theme.SentinelaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // O Context é preciso para aceder ao armazenamento
        val context = this

        setContent {
            SentinelaTheme {
                val ecra = remember { mutableStateOf("lista") }

                // A lista começa com os sites que estavam guardados
                val sites = remember {
                    val lista = mutableStateListOf<Site>()
                    lista.addAll(lerSites(context))
                    lista
                }

                if (ecra.value == "lista") {
                    ListaSitesScreen(
                        sites = sites,
                        onAdicionar = { ecra.value = "adicionar" },
                        onApagar = { site ->
                            sites.remove(site)
                            guardarSites(context, sites)   // guarda a lista sem o site apagado
                        }
                    )
                } else {
                    AdicionarSiteScreen(
                        onGuardar = { nome, url ->
                            sites.add(Site(nome, url, false))
                            guardarSites(context, sites)   // guarda a lista com o site novo
                            ecra.value = "lista"
                        },
                        onCancelar = { ecra.value = "lista" }
                    )
                }
            }
        }
    }
}