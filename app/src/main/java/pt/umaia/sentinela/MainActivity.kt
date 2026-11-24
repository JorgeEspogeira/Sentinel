package pt.umaia.sentinela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import pt.umaia.sentinela.ui.theme.SentinelaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // O Context é preciso para aceder ao armazenamento
        val context = this

        setContent {
            SentinelaTheme {
                val auth = remember { FirebaseAuth.getInstance() }
                val usuarioAtual = remember { mutableStateOf<FirebaseUser?>(auth.currentUser) }
                val ecra = remember { mutableStateOf("lista") }
                val siteAberto = remember { mutableStateOf(0) }   // posição do site que está aberto no detalhe

                // A lista começa com os sites que estavam guardados
                val sites = remember {
                    val lista = mutableStateListOf<Site>()
                    lista.addAll(lerSites(context))
                    verificarSempre(lista)   // verifica ao abrir e repete de 30 em 30 segundos
                    lista
                }

                if (usuarioAtual.value == null) {
                    LoginScreen(
                        onLoginSucesso = {
                            usuarioAtual.value = auth.currentUser
                        }
                    )
                } else {
                    if (ecra.value == "lista") {
                        ListaSitesScreen(
                            sites = sites,
                            onAdicionar = { ecra.value = "adicionar" },
                            onApagar = { site ->
                                sites.remove(site)
                                guardarSites(context, sites)   // guarda a lista sem o site apagado
                            },
                            onAbrir = { site ->
                                siteAberto.value = sites.indexOf(site)   // guarda qual foi escolhido
                                ecra.value = "detalhe"
                            },
                            onSair = {
                                auth.signOut()
                                usuarioAtual.value = null
                            }
                        )
                    } else if (ecra.value == "detalhe") {
                        DetalheSiteScreen(
                            site = sites[siteAberto.value],
                            onVoltar = { ecra.value = "lista" }
                        )
                    } else {
                        AdicionarSiteScreen(
                            onGuardar = { nome, url ->
                                sites.add(Site(nome, url, false))
                                guardarSites(context, sites)   // guarda a lista com o site novo
                                verificarTodos(sites)          // verifica logo o site novo
                                ecra.value = "lista"
                            },
                            onCancelar = { ecra.value = "lista" }
                        )
                    }
                }
            }
        }
    }
}