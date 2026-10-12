package br.com.canastra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
    val opening = if (maxOf(teamA, teamB) >= 2000) 90 else 50
    MaterialTheme(colorScheme = darkColorScheme(primary = Gold, background = Forest, surface = Color(0xFF0B4938))) {
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF031E19), Forest, Color(0xFF0C5942))))) {
            when (screen) {
                "home" -> HomeScreen({ screen = "score" }, { screen = "online" })
                "score" -> ScoreScreen(teamA, teamB, rounds, opening, { screen = "home" }, { showRound = true })
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
                    val a = aInput.toIntOrNull() ?: 0; val b = bInput.toIntOrNull() ?: 0
                    teamA += a; teamB += b; rounds = rounds + (a to b)
                    aInput = ""; bInput = ""; showRound = false
                }) { Text("SALVAR RODADA", color = Gold, fontWeight = FontWeight.Bold) } },
                dismissButton = { TextButton(onClick = { showRound = false }) { Text("Cancelar", color = Muted) } }
            )
        }
    }
}

@Composable
fun HomeScreen(onPlay: () -> Unit, onOnline: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(44.dp).background(Gold, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.SportsEsports, null, tint = Forest, modifier = Modifier.size(27.dp)) }
            Spacer(Modifier.width(10.dp))
            Column { Text("CANASTRA", color = Cream, fontWeight = FontWeight.Black, fontSize = 22.sp, letterSpacing = 3.sp); Text("CLUBE • CARTAS & ESTRATÉGIA", color = Gold, fontSize = 9.sp, letterSpacing = 1.5.sp) }
        }
        Spacer(Modifier.height(34.dp))
        Box(Modifier.fillMaxWidth().weight(1f).background(Brush.linearGradient(listOf(Color(0xFF17634B), Color(0xFF082D25))), RoundedCornerShape(30.dp)).border(1.dp, Color(0x5578B69A), RoundedCornerShape(30.dp)).padding(20.dp)) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("♠", color = Cream, fontSize = 45.sp); Text("A MESA ESTÁ", color = Muted, fontSize = 13.sp, letterSpacing = 4.sp)
                Text("ABERTA", color = Gold, fontSize = 38.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Spacer(Modifier.height(22.dp))
                Row(horizontalArrangement = Arrangement.spacedBy((-18).dp)) { PlayingCard("A", "♠", false); PlayingCard("7", "♥", true); PlayingCard("K", "♣", false) }
                Spacer(Modifier.height(26.dp)); Text("Sua próxima canastra começa aqui.", color = Cream, textAlign = TextAlign.Center, fontSize = 15.sp)
                Text("Estratégia, parceria e aquela virada no placar.", color = Muted, textAlign = TextAlign.Center, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = onOnline, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Forest)) {
            Icon(Icons.Default.Groups, null); Spacer(Modifier.width(10.dp)); Text("JOGAR ONLINE", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onPlay, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(18.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF76A994)), colors = ButtonDefaults.outlinedButtonColors(contentColor = Cream)) {
            Icon(Icons.Default.EmojiEvents, null); Spacer(Modifier.width(10.dp)); Text("PLACAR E PARTIDA", fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp)); Text("DESAFIE • SOME • VÁ ATÉ A VITÓRIA", color = Muted, fontSize = 10.sp, letterSpacing = 1.4.sp)
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
            Column(Modifier.weight(1f)) { Text("MÍNIMO PARA BAIXAR", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp); Text("$opening pontos", color = Gold, fontSize = 25.sp, fontWeight = FontWeight.Black) }
            Text(if (maxOf(teamA, teamB) >= 2000) "REGRA 2.000 ATIVA" else "ATÉ 1.999 PTS", color = if (maxOf(teamA, teamB) >= 2000) Gold else Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        if (maxOf(teamA, teamB) >= 2000) { Spacer(Modifier.height(12.dp)); Text("✦ MARCO DE 2.000 PONTOS! A abertura agora exige $opening pontos.", color = Gold, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        Spacer(Modifier.height(18.dp))
        Button(onClick = onAdd, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Forest)) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("LANÇAR PONTUAÇÃO DA RODADA", fontWeight = FontWeight.Black) }
        Spacer(Modifier.height(20.dp)); Text("HISTÓRICO DE RODADAS", color = Cream, fontWeight = FontWeight.Bold, letterSpacing = 1.sp); Spacer(Modifier.height(8.dp))
        if (rounds.isEmpty()) Box(Modifier.fillMaxWidth().weight(1f).background(Color(0x331A6650), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("♧", color = Gold, fontSize = 38.sp); Text("A primeira rodada começa agora", color = Muted) }
        } else Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            rounds.forEachIndexed { i, pair -> Row(Modifier.fillMaxWidth().background(Color(0xFF103D32), RoundedCornerShape(12.dp)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("RODADA ${i + 1}", color = Muted, modifier = Modifier.weight(1f), fontSize = 12.sp); Text("+${pair.first}", color = Cream, fontWeight = FontWeight.Bold); Spacer(Modifier.width(20.dp)); Text("+${pair.second}", color = Gold, fontWeight = FontWeight.Bold)
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
                Spacer(Modifier.height(10.dp)); Text("A conexão online precisa ser vinculada a um servidor de partidas. A tela está preparada, mas a sala ainda não conecta sem configurar esse serviço.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
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
