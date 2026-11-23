package pt.umaia.sentinela

import java.net.HttpURLConnection
import java.net.URL

// Verifica se um site está online. Devolve true (online) ou false (offline).
fun verificarSite(urlOriginal: String): Boolean {
    // Se o utilizador escreveu só "www.google.pt", acrescenta https://
    var url = urlOriginal
    if (!url.startsWith("http")) {
        url = "https://" + url
    }

    try {
        val ligacao = URL(url).openConnection() as HttpURLConnection
        ligacao.connectTimeout = 5000   // espera no máximo 5 segundos
        ligacao.readTimeout = 5000
        val codigo = ligacao.responseCode   // 200 = OK, 404 = não encontrado, etc.
        ligacao.disconnect()
        return codigo in 200..399           // de 200 a 399 conta como online
    } catch (e: Exception) {
        return false                        // qualquer erro = offline
    }
}

// Verifica todos os sites da lista, um a um
fun verificarTodos(sites: MutableList<Site>) {
    // Thread = tarefa em paralelo. O Android não deixa usar a internet
    // na tarefa principal (bloqueava o ecrã), por isso usamos outra.
    Thread {
        for (i in sites.indices) {
            if (i < sites.size) {   // evita erro se um site for apagado entretanto
                val online = verificarSite(sites[i].url)
                sites[i] = sites[i].copy(online = online)   // troca pela cópia com o estado novo
            }
        }
    }.start()
}