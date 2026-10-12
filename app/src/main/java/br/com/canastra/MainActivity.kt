package br.com.canastra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Forest = Color(0xFF062F26)
private val Gold = Color(0xFFFFC857)
private val Cream = Color(0xFFFFF8E9)
private val Muted = Color(0xFFB8D1C5)
private val Red = Color(0xFFC84242)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CanastraApp() }
    }
}

@Composable
fun CanastraApp() {
    var screen by remember { mutableStateOf("home") }
    var teamA by remember { mutableIntStateOf(0) }
    var teamB by remember { mutableIntStateOf(0) }
    var rounds by remember { mutableStateOf(listOf<Pair<Int, Int>>()) }
    var showRound by remember { mutableStateOf(false) }
    var aInput by remember { mutableStateOf("") }
    var bInput by remember { mutableStateOf("") }
    var roomCode by remember { mutableStateOf("") }
    val opening = if (maxOf(teamA, teamB) >= 2000) 150 else 0
    MaterialTheme(colorScheme = darkColorScheme(primary = Gold, background = Forest, surface = Color(0xFF0B4938))) {
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF031E19), Forest, Color(0xFF0C5942))))) {
            when (screen) {
                "home" -> HomeScreen({ screen = "score" }, { screen = "online" }, { screen = "offline" })
                "score" -> ScoreScreen(teamA, teamB, rounds, opening, { screen = "home" }, { showRound = true })
                "offline" -> OfflineGameScreen({ screen = "home" }, { a, b ->
                    teamA += a
                    teamB += b
                    rounds = rounds + (a to b)
                })
                else -> OnlineScreen(roomCode, { roomCode = it }, { screen = "home" })
            }
            if (showRound) AlertDialog(
                onDismissRequest = { showRound = false }, containerColor = Color(0xFF103D32),
                title = { Text("Fechar rodada", color = Cream, fontWeight = FontWeight.Bold) },
                text = { Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Pontuação da rodada por dupla", color = Muted)
                    OutlinedTextField(aInput, { aInput = it }, label = { Text("Dupla 1") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(bInput, { bInput = it }, label = { Text("Dupla 2") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                } },
                confirmButton = { TextButton(onClick = {
                    val a = aInput.toIntOrNull() ?: 0
                    val b = bInput.toIntOrNull() ?: 0
                    teamA += a
                    teamB += b
                    rounds = rounds + (a to b)
                    aInput = ""
                    bInput = ""
                    showRound = false
                }) { Text("SALVAR RODADA", color = Gold, fontWeight = FontWeight.Bold) } },
                dismissButton = { TextButton(onClick = { showRound = false }) { Text("Cancelar", color = Muted) } }
            )
        }
    }
}

@Composable
fun HomeScreen(onPlay: () -> Unit, onOnline: () -> Unit, onOffline: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(44.dp).background(Gold, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.SportsEsports, null, tint = Forest, modifier = Modifier.size(27.dp)) }
            Spacer(Modifier.width(10.dp))
            Column { Text("CANASTRA", color = Cream, fontWeight = FontWeight.Black, fontSize = 22.sp, letterSpacing = 3.sp); Text("CLUBE • CARTAS & ESTRATÉGIA", color = Gold, fontSize = 9.sp, letterSpacing = 1.5.sp) }
        }
        Spacer(Modifier.height(26.dp))
        Box(Modifier.fillMaxWidth().weight(1f).background(Brush.linearGradient(listOf(Color(0xFF17634B), Color(0xFF082D25))), RoundedCornerShape(30.dp)).border(1.dp, Color(0x5578B69A), RoundedCornerShape(30.dp)).padding(20.dp)) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("♠", color = Cream, fontSize = 45.sp)
                Text("A MESA ESTÁ", color = Muted, fontSize = 13.sp, letterSpacing = 4.sp)
                Text("ABERTA", color = Gold, fontSize = 38.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Spacer(Modifier.height(22.dp))
                Row(horizontalArrangement = Arrangement.spacedBy((-18).dp)) { PlayingCard("A", "♠", false); PlayingCard("7", "♥", true); PlayingCard("K", "♣", false) }
                Spacer(Modifier.height(26.dp))
                Text("Sua próxima canastra começa aqui.", color = Cream, textAlign = TextAlign.Center, fontSize = 15.sp)
                Text("Estratégia, parceria e aquela virada no placar.", color = Muted, textAlign = TextAlign.Center, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(18.dp))
        Button(onClick = onOffline, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Forest)) {
            Icon(Icons.Default.SportsEsports, null); Spacer(Modifier.width(10.dp)); Text("JOGAR OFFLINE • CONTRA A MÁQUINA", fontWeight = FontWeight.Black, letterSpacing = .3.sp)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onOnline, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(18.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF76A994)), colors = ButtonDefaults.outlinedButtonColors(contentColor = Cream)) {
            Icon(Icons.Default.Groups, null); Spacer(Modifier.width(10.dp)); Text("JOGAR ONLINE", fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onPlay, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(18.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF76A994)), colors = ButtonDefaults.outlinedButtonColors(contentColor = Cream)) {
            Icon(Icons.Default.EmojiEvents, null); Spacer(Modifier.width(10.dp)); Text("PLACAR E PARTIDA", fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp)); Text("DESAFIE • SOME • VÁ ATÉ A VITÓRIA", color = Muted, fontSize = 10.sp, letterSpacing = 1.4.sp)
    }
}

@Composable
fun PlayingCard(rank: String, suit: String, red: Boolean) {
    Column(Modifier.width(78.dp).height(116.dp).background(Cream, RoundedCornerShape(12.dp)).border(1.dp, Color(0xFFE6DCC9), RoundedCornerShape(12.dp)).padding(8.dp), verticalArrangement = Arrangement.SpaceBetween) {
        Text(rank, color = if (red) Red else Forest, fontWeight = FontWeight.Black, fontSize = 21.sp)
        Text(suit, color = if (red) Red else Forest, fontSize = 35.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text(rank, color = if (red) Red else Forest, fontWeight = FontWeight.Black, fontSize = 18.sp, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun ScoreScreen(teamA: Int, teamB: Int, rounds: List<Pair<Int, Int>>, opening: Int, onBack: () -> Unit, onAdd: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar", tint = Cream) }
            Column { Text("PLACAR DA PARTIDA", color = Cream, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp); Text("Pontuação acumulada por dupla", color = Muted, fontSize = 12.sp) }
        }
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { ScoreCard("DUPLA VERDE", teamA, Color(0xFF0B4938), Modifier.weight(1f)); ScoreCard("DUPLA DOURADA", teamB, Color(0xFF59451B), Modifier.weight(1f)) }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth().background(Color(0xFF123D31), RoundedCornerShape(16.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(if (opening == 150) "MÍNIMO PARA BAIXAR • MARRA" else "SEM MÍNIMO PARA BAIXAR", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp); Text(if (opening == 150) "150 pontos" else "Livre", color = Gold, fontSize = 25.sp, fontWeight = FontWeight.Black) }
            Text(if (opening == 150) "MARRA ATIVA" else "JOGO NORMAL", color = if (opening == 150) Gold else Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        if (maxOf(teamA, teamB) >= 2000) { Spacer(Modifier.height(12.dp)); Text("✦ MARRA ATIVA! Para baixar jogos, agora é necessário fazer no mínimo $opening pontos.", color = Gold, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        Spacer(Modifier.height(18.dp))
        Button(onClick = onAdd, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Forest)) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("LANÇAR PONTUAÇÃO DA RODADA", fontWeight = FontWeight.Black) }
        Spacer(Modifier.height(20.dp)); Text("HISTÓRICO DE RODADAS", color = Cream, fontWeight = FontWeight.Bold, letterSpacing = 1.sp); Spacer(Modifier.height(8.dp))
        if (rounds.isEmpty()) Box(Modifier.fillMaxWidth().weight(1f).background(Color(0x331A6650), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("♧", color = Gold, fontSize = 38.sp); Text("A primeira rodada começa agora", color = Muted) }
        } else Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            rounds.forEachIndexed { i, pair -> Row(Modifier.fillMaxWidth().background(Color(0xFF103D32), RoundedCornerShape(12.dp)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("RODADA " + (i + 1), color = Muted, modifier = Modifier.weight(1f), fontSize = 12.sp); Text("+" + pair.first, color = Cream, fontWeight = FontWeight.Bold); Spacer(Modifier.width(20.dp)); Text("+" + pair.second, color = Gold, fontWeight = FontWeight.Bold)
            } }
        }
        Text("Canastra • placar local", color = Muted, fontSize = 10.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp))
    }
}

@Composable
fun ScoreCard(label: String, score: Int, color: Color, modifier: Modifier = Modifier) {
    Column(modifier.background(color, RoundedCornerShape(20.dp)).padding(16.dp)) {
        Text(label, color = Cream.copy(alpha = .8f), fontSize = 10.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp))
        Text(score.toString(), color = Gold, fontSize = 36.sp, fontWeight = FontWeight.Black); Text("PONTOS", color = Muted, fontSize = 9.sp, letterSpacing = 2.sp)
        Spacer(Modifier.height(8.dp)); LinearProgressIndicator(progress = { (score / 3000f).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth(), color = Gold, trackColor = Color.White.copy(alpha = .15f))
    }
}

data class GameCard(val id: Int, val rank: Int, val suit: String, val joker: Boolean = false) {
    val label: String get() = if (joker) "★" else when (rank) { 1 -> "A"; 11 -> "J"; 12 -> "Q"; 13 -> "K"; else -> rank.toString() }
    val isWild: Boolean get() = joker || rank == 2
    val points: Int get() = when { joker -> 50; rank == 1 -> 15; rank == 2 -> 10; rank >= 8 -> 10; else -> 5 }
    val displaySuit: String get() = when (suit) { "S" -> "♠"; "H" -> "♥"; "D" -> "♦"; "C" -> "♣"; else -> "★" }
    val isRed: Boolean get() = suit == "H" || suit == "D"
}

private fun newDeck(): MutableList<GameCard> {
    val cards = mutableListOf<GameCard>()
    var id = 0
    repeat(2) {
        listOf("S", "H", "D", "C").forEach { suit ->
            (1..13).forEach { rank -> cards.add(GameCard(id++, rank, suit)) }
        }
        repeat(2) { cards.add(GameCard(id++, 0, "J", true)) }
    }
    return cards.shuffled().toMutableList()
}

private fun isValidMeld(cards: List<GameCard>): Boolean {
    if (cards.size < 3) return false
    val wilds = cards.count { it.isWild }
    val natural = cards.filterNot { it.isWild }
    if (natural.isEmpty()) return true
    val sameRank = natural.map { it.rank }.distinct().size == 1
    if (sameRank && natural.size + wilds <= 4) return true
    val sameSuit = natural.map { it.suit }.distinct().size == 1
    if (!sameSuit) return false
    val ranks = natural.map { if (it.rank == 1) 14 else it.rank }.distinct().sorted()
    if (ranks.size != natural.size) return false
    val gapsNeeded = ranks.zipWithNext().sumOf { (a, b) -> (b - a - 1).coerceAtLeast(0) }
    return gapsNeeded <= wilds && cards.size <= 14
}

@Composable
fun OfflineGameScreen(onBack: () -> Unit, onRoundScored: (Int, Int) -> Unit) {
    var deck by remember { mutableStateOf(newDeck()) }
    var hand by remember { mutableStateOf(deck.take(11)) }
    var stock by remember { mutableStateOf(deck.drop(11)) }
    var discard by remember { mutableStateOf(emptyList<GameCard>()) }
    var selected by remember { mutableStateOf(setOf<Int>()) }
    var playerMelds by remember { mutableStateOf(listOf<List<GameCard>>()) }
    var aiMelds by remember { mutableStateOf(listOf<List<GameCard>>()) }
    var playerPoints by remember { mutableIntStateOf(0) }
    var aiPoints by remember { mutableIntStateOf(0) }
    var hasDrawn by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Sua vez! Compre uma carta do monte ou do descarte.") }
    var gameOver by remember { mutableStateOf(false) }
    var roundNumber by remember { mutableIntStateOf(1) }
    val opening = if (maxOf(playerPoints, aiPoints) >= 2000) 150 else 0
    fun restart() {
        val fresh = newDeck()
        deck = fresh
        hand = fresh.take(11)
        stock = fresh.drop(11)
        discard = emptyList()
        selected = emptySet()
        playerMelds = emptyList()
        aiMelds = emptyList()
        playerPoints = 0
        aiPoints = 0
        hasDrawn = false
        message = "Sua vez! Compre uma carta do monte ou do descarte."
        gameOver = false
        roundNumber = 1
    }
    fun aiTurn() {
        var aiHand = (hand /* placeholder only to keep state separate */)
        var computerCards = aiMelds.flatten()
        // The computer's concealed hand is stored separately below.
    }
    var aiHand by remember { mutableStateOf(emptyList<GameCard>()) }
    LaunchedEffect(Unit) {
        val fresh = newDeck()
        deck = fresh
        hand = fresh.take(11)
        aiHand = fresh.drop(11).take(11)
        stock = fresh.drop(22)
        discard = emptyList()
    }
    fun drawFromStock() {
        if (hasDrawn || gameOver) return
        if (stock.isEmpty()) { message = "O monte acabou. Inicie uma nova partida."; gameOver = true; return }
        hand = hand + stock.first()
        stock = stock.drop(1)
        hasDrawn = true
        message = "Escolha cartas para baixar um jogo ou descarte uma carta."
    }
    fun drawDiscard() {
        if (hasDrawn || gameOver) return
        if (discard.isEmpty()) { message = "Ainda não há cartas no descarte."; return }
        if (discard.last().isWild) {
            message = if (discard.last().rank == 2) "Lixo trancado pelo 2 (coringa sujo)! Compre do monte." else "Lixo trancado pelo coringa real! Compre do monte."
            return
        }
        hand = hand + discard.last()
        discard = discard.dropLast(1)
        hasDrawn = true
        message = "Você pegou o descarte. Agora baixe um jogo ou descarte uma carta."
    }
    fun meldSelected() {
        val chosen = hand.filter { selected.contains(it.id) }
        if (!hasDrawn) { message = "Compre uma carta primeiro."; return }
        if (!isValidMeld(chosen)) { message = "Escolha 3 ou mais cartas que formem um jogo válido."; return }
        val value = chosen.sumOf { it.points }
        if (playerMelds.isEmpty() && opening > 0 && value < opening) { message = "MARRA ativa: para baixar pela primeira vez, precisa somar pelo menos $opening pontos."; return }
        playerMelds = playerMelds + listOf(chosen)
        playerPoints += value
        hand = hand.filterNot { selected.contains(it.id) }
        selected = emptySet()
        message = "Jogo baixado! +$value pontos."
    }
    fun discardSelected() {
        if (!hasDrawn || gameOver) { message = "Compre uma carta antes de descartar."; return }
        if (selected.size != 1) { message = "Selecione exatamente uma carta para descartar."; return }
        val card = hand.firstOrNull { selected.contains(it.id) } ?: return
        hand = hand.filterNot { it.id == card.id }
        discard = discard + card
        selected = emptySet()
        hasDrawn = false
        // Computer plays offline: draws, tries a legal meld, and discards a low-value card.
        var computer = aiHand
        if (stock.isNotEmpty()) {
            computer = computer + stock.first()
            stock = stock.drop(1)
        }
        val sorted = computer.sortedByDescending { it.points }
        val candidate = sorted.groupBy { it.rank }.values.firstOrNull { it.size >= 3 }
            ?: sorted.groupBy { it.suit }.values.firstOrNull { group ->
                group.size >= 3 && group.map { if (it.rank == 1) 14 else it.rank }.sorted().zipWithNext().all { it.second - it.first == 1 }
            }
        if (candidate != null && (aiMelds.isNotEmpty() || candidate.sumOf { it.points } >= (if (maxOf(playerPoints, aiPoints) >= 2000) 150 else 50))) {
            aiMelds = aiMelds + listOf(candidate)
            aiPoints += candidate.sumOf { it.points }
            computer = computer.filterNot { c -> candidate.any { it.id == c.id } }
            message = "A máquina baixou um jogo de " + candidate.sumOf { it.points } + " pontos."
        } else {
            message = "Sua vez novamente. Compre uma carta."
        }
        if (computer.isNotEmpty()) {
            val discardCard = computer.minByOrNull { it.points }!!
            computer = computer.filterNot { it.id == discardCard.id }
            discard = discard + discardCard
        }
        aiHand = computer
        if (hand.isEmpty() || aiHand.isEmpty()) {
            val playerTotal = playerPoints + hand.sumOf { it.points }
            val aiTotal = aiPoints + aiHand.sumOf { it.points }
            onRoundScored(playerTotal, aiTotal)
            message = if (playerTotal >= aiTotal) "Rodada encerrada: você venceu!" else "Rodada encerrada: a máquina venceu."
            gameOver = true
        }
    }
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 14.dp, vertical = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar", tint = Cream) }
            Column(Modifier.weight(1f)) {
                Text("MESA OFFLINE", color = Cream, fontWeight = FontWeight.Black, fontSize = 19.sp, letterSpacing = 1.sp)
                Text("Você × Máquina • rodada $roundNumber", color = Muted, fontSize = 12.sp)
            }
            TextButton(onClick = { restart() }) { Text("NOVA", color = Gold, fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MiniScore("VOCÊ", playerPoints, Modifier.weight(1f))
            MiniScore("MÁQUINA", aiPoints, Modifier.weight(1f))
            Column(Modifier.weight(1f).background(Color(0xFF123D31), RoundedCornerShape(14.dp)).padding(10.dp)) {
                Text(if (opening == 150) "MARRA • MÍNIMO" else "BAIXA LIVRE", color = Muted, fontSize = 9.sp)
                Text(if (opening == 150) "150 pts" else "Livre", color = Gold, fontSize = 18.sp, fontWeight = FontWeight.Black)
                Text(if (opening == 150) "ATIVA" else "SEM MÍNIMO", color = Muted, fontSize = 9.sp)
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF104332)), shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                Text(message, color = Cream, fontSize = 12.sp, lineHeight = 17.sp)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("MONTE: " + stock.size, color = Muted, fontSize = 11.sp)
                    Text("•", color = Gold)
                    Text("DESCARTE: " + (discard.lastOrNull()?.let { it.label + it.displaySuit } ?: "vazio") + if (discard.lastOrNull()?.isWild == true) " 🔒" else "", color = Gold, fontSize = 11.sp)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("JOGOS BAIXADOS", color = Cream, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MeldPile("VOCÊ", playerMelds)
            MeldPile("MÁQUINA", aiMelds)
        }
        Spacer(Modifier.height(10.dp))
        Text("SUA MÃO • toque para selecionar", color = Cream, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = .5.sp)
        Text("Selecionadas: " + selected.size + " • compre antes de jogar", color = Muted, fontSize = 11.sp)
        Spacer(Modifier.height(6.dp))
        Column(Modifier.weight(1f).fillMaxWidth().background(Color(0x44217A5B), RoundedCornerShape(18.dp)).padding(8.dp)) {
            hand.chunked(4).forEach { rowCards ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    rowCards.forEach { card ->
                        MiniPlayingCard(card, selected.contains(card.id), Modifier.weight(1f)) {
                            if (!gameOver) selected = if (selected.contains(card.id)) selected - card.id else selected + card.id
                        }
                    }
                    repeat(4 - rowCards.size) { Spacer(Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(6.dp))
            }
            if (hand.isEmpty()) Text("Sem cartas na mão", color = Muted, modifier = Modifier.padding(12.dp))
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { drawFromStock() }, enabled = !hasDrawn && !gameOver, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Cream)) { Text("COMPRAR", fontWeight = FontWeight.Bold) }
            OutlinedButton(onClick = { drawDiscard() }, enabled = !hasDrawn && discard.isNotEmpty() && discard.lastOrNull()?.isWild != true && !gameOver, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Cream)) { Text(if (discard.lastOrNull()?.isWild == true) "LIXO TRANCADO 🔒" else "PEGAR DESCARTE", fontWeight = FontWeight.Bold, fontSize = 10.sp) }
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { meldSelected() }, enabled = hasDrawn && selected.size >= 3 && !gameOver, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Forest)) { Text("BAIXAR JOGO", fontWeight = FontWeight.Black, fontSize = 11.sp) }
            Button(onClick = { discardSelected() }, enabled = hasDrawn && selected.size == 1 && !gameOver, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF17634B), contentColor = Cream)) { Text("DESCARTAR", fontWeight = FontWeight.Black, fontSize = 11.sp) }
        }
        if (gameOver) {
            Spacer(Modifier.height(6.dp))
            Button(onClick = { restart() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Forest)) { Text("JOGAR OUTRA PARTIDA", fontWeight = FontWeight.Black) }
        }
    }
}

@Composable
fun MiniScore(label: String, score: Int, modifier: Modifier = Modifier) {
    Column(modifier.background(Color(0xFF0B4938), RoundedCornerShape(14.dp)).padding(10.dp)) {
        Text(label, color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(score.toString(), color = Gold, fontSize = 23.sp, fontWeight = FontWeight.Black)
        Text("PONTOS", color = Muted, fontSize = 8.sp, letterSpacing = 1.sp)
    }
}

@Composable
fun MeldPile(label: String, melds: List<List<GameCard>>) {
    Column(Modifier.widthIn(min = 120.dp).background(Color(0xFF103D32), RoundedCornerShape(12.dp)).padding(8.dp)) {
        Text(label + " • " + melds.size, color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        if (melds.isEmpty()) Text("Nenhum jogo", color = Muted, fontSize = 10.sp)
        melds.forEach { meld ->
            Text(meld.joinToString(" ") { it.label + it.displaySuit }, color = Cream, fontSize = 11.sp)
        }
    }
}

@Composable
fun MiniPlayingCard(card: GameCard, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier.height(96.dp).background(if (selected) Color(0xFFFFD778) else Cream, RoundedCornerShape(9.dp))
        .border(if (selected) 2.dp else 1.dp, if (selected) Gold else Color(0xFFE6DCC9), RoundedCornerShape(9.dp))
        .clickable(onClick = onClick).padding(5.dp), verticalArrangement = Arrangement.SpaceBetween) {
        Text(card.label, color = if (card.isRed) Red else Forest, fontWeight = FontWeight.Black, fontSize = 15.sp)
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(card.displaySuit, color = if (card.isRed) Red else Forest, fontSize = 25.sp)
            if (card.rank == 2) Text("CORINGA SUJO", color = if (card.isRed) Red else Forest, fontSize = 5.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            if (card.joker) Text("CORINGA REAL", color = Gold, fontSize = 5.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
        }
        Text(card.label, color = if (card.isRed) Red else Forest, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.align(Alignment.End))
    }
}

@Composable
fun OnlineScreen(code: String, onCode: (String) -> Unit, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(24.dp)) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar", tint = Cream) }
        Spacer(Modifier.height(20.dp)); Icon(Icons.Default.Groups, null, tint = Gold, modifier = Modifier.size(52.dp))
        Spacer(Modifier.height(14.dp)); Text("MESA ONLINE", color = Cream, fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp); Text("Jogue com amigos em tempo real.", color = Muted, fontSize = 14.sp)
        Spacer(Modifier.height(28.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF103D32)), shape = RoundedCornerShape(22.dp)) {
            Column(Modifier.padding(20.dp)) {
                Text("CRIAR OU ENTRAR EM UMA SALA", color = Gold, fontWeight = FontWeight.Bold); Spacer(Modifier.height(12.dp))
                OutlinedTextField(code, onCode, modifier = Modifier.fillMaxWidth(), label = { Text("Código da sala") }, placeholder = { Text("Ex.: A7K2Q") }, singleLine = true)
                Spacer(Modifier.height(14.dp))
                Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, disabledContainerColor = Color(0xFF5E624F)), shape = RoundedCornerShape(14.dp)) { Text("CONECTAR À MESA", fontWeight = FontWeight.Black) }
                Spacer(Modifier.height(10.dp)); Text("A conexão online precisa ser vinculada a um servidor de partidas. Enquanto isso, você pode jogar offline contra a máquina.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
            }
        }
        Spacer(Modifier.height(18.dp)); Text("PREVISTO PARA O MULTIPLAYER", color = Cream, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        FeatureLine("♧", "Salas privadas por código"); FeatureLine("♙", "Quatro jogadores e duas duplas"); FeatureLine("✦", "Placar sincronizado em tempo real"); FeatureLine("♤", "Reconexão ao cair a internet")
    }
}
@Composable
fun FeatureLine(symbol: String, label: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(36.dp).background(Color(0xFF174B3B), CircleShape), contentAlignment = Alignment.Center) { Text(symbol, color = Gold, fontSize = 18.sp) }
        Spacer(Modifier.width(12.dp)); Text(label, color = Cream, fontSize = 14.sp)
    }
}
