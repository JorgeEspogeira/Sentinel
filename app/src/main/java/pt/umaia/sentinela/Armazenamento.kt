package pt.umaia.sentinela

import android.content.Context

// Guarda a lista de sites no telemóvel.
// Cada site fica numa linha, assim: nome;url
fun guardarSites(context: Context, sites: List<Site>) {
    // Constrói um texto com uma linha por site
    var texto = ""
    for (site in sites) {
        texto = texto + site.nome + ";" + site.url + "\n"
    }

    // SharedPreferences: guarda pequenos dados na app, com uma chave
    val prefs = context.getSharedPreferences("sentinela", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString("sites", texto)   // chave "sites", valor: o texto
    editor.apply()                     // grava
}

// Lê os sites guardados e devolve uma lista (vazia se não houver nada)
fun lerSites(context: Context): List<Site> {
    val prefs = context.getSharedPreferences("sentinela", Context.MODE_PRIVATE)
    val texto = prefs.getString("sites", "")

    val lista = mutableListOf<Site>()
    if (texto != null) {
        val linhas = texto.split("\n")        // separa o texto em linhas
        for (linha in linhas) {
            val partes = linha.split(";")     // separa o nome e o url
            if (partes.size == 2) {           // ignora linhas vazias ou erradas
                lista.add(Site(partes[0], partes[1], false))
            }
        }
    }
    return lista
}