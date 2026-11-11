package pt.umaia.sentinela

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Ecrã para adicionar um site novo
@Composable
fun AdicionarSiteScreen(
    onGuardar: (String, String) -> Unit,   // chamada ao guardar, com o nome e o URL
    onCancelar: () -> Unit                 // chamada ao cancelar
) {
    // O que o utilizador escreve em cada campo. Começam vazios.
    val nome = remember { mutableStateOf("") }
    val url = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Novo site", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Campo de texto: mostra nome.value e, a cada letra escrita,
        // guarda o texto novo (it) em nome.value
        OutlinedTextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = url.value,
            onValueChange = { url.value = it },
            label = { Text("URL (ex: www.google.pt)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Só guarda se os dois campos tiverem texto
                if (nome.value.isNotBlank() && url.value.isNotBlank()) {
                    onGuardar(nome.value, url.value)
                }
            }
        ) {
            Text("Guardar")
        }

        Button(onClick = onCancelar) {
            Text("Cancelar")
        }
    }
}