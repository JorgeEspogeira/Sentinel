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

// Verifica todos os sites uma vez e mede o tempo que cada um demora a responder
// (tem de ser chamada dentro de uma Thread)
fun verificarLista(sites: MutableList<Site>) {
    for (i in sites.indices) {
        // se entretanto apagaste um site, a lista ficou mais pequena
        if (i < sites.size) {
            val inicio = System.currentTimeMillis()   // hora antes do pedido
            val online = verificarSite(sites[i].url)
            val tempo = (System.currentTimeMillis() - inicio).toInt()   // hora depois menos hora antes
            if (i < sites.size) {
                sites[i] = sites[i].copy(online = online, tempoMs = tempo)   // troca pela cópia com o estado novo
            }
        }
    }
}

// Verifica todos os sites uma vez
fun verificarTodos(sites: MutableList<Site>) {
    // Thread = tarefa em paralelo. O Android não deixa usar a internet
    // na tarefa principal (bloqueava o ecrã), por isso usamos outra.
    Thread {
        verificarLista(sites)
    }.start()
}

// Verifica todos os sites e repete de 30 em 30 segundos
fun verificarSempre(sites: MutableList<Site>) {
    Thread {
        // este ciclo nunca acaba: a app continua a verificar enquanto estiver aberta
        while (true) {
            verificarLista(sites)
            // espera 30 segundos (30000 milissegundos) antes de voltar a verificar
            Thread.sleep(30000)
        }
    }.start()
}