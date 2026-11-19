package com.ushort.app

import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.content.Context
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext

private val ink = Color(0xFF21123E)
private val lilac = Color(0xFFDCCBFF)
private val pink = Color(0xFFD66BCB)
private val cyan = Color(0xFF49C9F0)

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { App() } }
}
private enum class Tab(val title:String) { HOME("Home"), CREATE("Create"), AUTO("Auto"), MANUAL("Manual"), EDIT("Edit"), AI("AI"), PROFILE("Profile") }

@Composable fun App() {
 var tab by remember { mutableStateOf(Tab.HOME) }
 var video by remember { mutableStateOf<Uri?>(null) }
 val pick = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { video = it }
 Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF271044),Color(0xFF7435A8),Color(0xFFB75BAE),Color(0xFF253F91))))) {
  Column(Modifier.fillMaxSize()) {
   Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment=Alignment.CenterVertically) {
    Box(Modifier.size(38.dp).clip(RoundedCornerShape(13.dp)).background(Brush.linearGradient(listOf(pink,Color(0xFF7839D1),cyan))), contentAlignment=Alignment.Center) { Icon(Icons.Default.PlayArrow,null,tint=Color.White) }
    Spacer(Modifier.width(10.dp)); Text("U SHORT",color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold)
    Spacer(Modifier.weight(1f)); Text("CREATE • EDIT • AI",color=lilac,fontSize=9.sp)
   }
   AnimatedContent(
    targetState=tab,
    transitionSpec={ (fadeIn()+slideInHorizontally { it/10 }) togetherWith (fadeOut()+slideOutHorizontally { -it/10 }) },
    label="main_navigation",
    modifier=Modifier.weight(1f)
   ) { page ->
    when(page) {
     Tab.HOME -> Home({tab=Tab.CREATE},{tab=Tab.CREATE},{tab=Tab.EDIT})
     Tab.CREATE -> CreateHub(
      auto={tab=Tab.AUTO},
      manual={tab=Tab.MANUAL},
      chooseVideo={pick.launch(arrayOf("video/*"))},
      selected=video!=null,
      openAI={tab=Tab.AI}
     )
     Tab.AUTO -> Auto(video) { pick.launch(arrayOf("video/*")) }
     Tab.MANUAL -> Manual(video) { pick.launch(arrayOf("video/*")) }
     Tab.EDIT -> Pro(video) { pick.launch(arrayOf("video/*")) }
     Tab.AI -> AssistantScreen()
     Tab.PROFILE -> Profile()
    }
   }
   Row(Modifier.fillMaxWidth().padding(9.dp).clip(RoundedCornerShape(24.dp)).background(Color(0x5530195B)).border(1.dp,Color.White.copy(.2f),RoundedCornerShape(24.dp)).padding(vertical=9.dp),horizontalArrangement=Arrangement.SpaceAround) {
    listOf(Tab.HOME to Icons.Default.Home,Tab.CREATE to Icons.Default.Bolt,Tab.EDIT to Icons.Default.Tune,Tab.AI to Icons.Default.AutoAwesome,Tab.PROFILE to Icons.Default.Person).forEach { (t,ic) ->
     Column(Modifier.clickable { tab=t }.padding(horizontal=7.dp),horizontalAlignment=Alignment.CenterHorizontally) {
      Icon(ic,null,tint=if(tab==t || (t==Tab.CREATE && (tab==Tab.AUTO || tab==Tab.MANUAL))) Color.White else lilac.copy(.7f),modifier=Modifier.size(20.dp))
      Text(t.title,color=if(tab==t || (t==Tab.CREATE && (tab==Tab.AUTO || tab==Tab.MANUAL))) Color.White else lilac.copy(.7f),fontSize=9.sp)
     }
    }
   }
  }
 }
}

@Composable private fun CreateHub(auto:()->Unit,manual:()->Unit,chooseVideo:()->Unit,selected:Boolean,openAI:()->Unit) {
 var query by remember { mutableStateOf("") }
 val groups=listOf(
  "CREATE" to listOf("Auto Shorts","Manual Shorts","AI Script Writer","AI Storyboard"),
  "EDIT" to listOf("Video Editor","Timeline","Transitions","Effects","Text & Captions","Audio Pro"),
  "COLOR LAB" to listOf("Color Wheels","Curves","HSL","LUTs","Color Mixer","Scopes"),
  "AI TOOLS" to listOf("Hook Master","Retention Booster","Background Remover","Video Enhancer","Voice Dubbing","Thumbnail Battle"),
  "DISCOVER" to listOf("Viral Hunter","Trending Topics","Competitor Intelligence","Content Ideas"),
  "PUBLISH" to listOf("Social Accounts","Content Calendar","Auto Scheduler","Smart Repost","Publishing Queue"),
  "ANALYTICS" to listOf("Channel Performance","Video Statistics","Audience Insights","A/B Experiments")
 )
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Heading("Tool Library","Find every workspace in one organized place.")
  OutlinedTextField(query,{query=it},modifier=Modifier.fillMaxWidth(),singleLine=true,placeholder={Text("Search tools…")},leadingIcon={Icon(Icons.Default.Search,null,tint=Color.White)})
  Panel {
   Text("START CREATING",color=lilac,fontSize=10.sp,fontWeight=FontWeight.Bold)
   Action("＋  Import video from phone",chooseVideo,true)
   Text(if(selected) "A video is selected for preview." else "Choose a video to begin.",color=lilac,fontSize=11.sp)
   Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
    Box(Modifier.weight(1f)){Action("Auto Shorts",auto,false)}
    Box(Modifier.weight(1f)){Action("Manual Shorts",manual,false)}
   }
  }
  groups.forEach { (name,items) ->
   val filtered=items.filter { it.contains(query,true) }
   if(filtered.isNotEmpty()) Panel {
    Row(verticalAlignment=Alignment.CenterVertically) {
     Text(name,color=Color.White,fontWeight=FontWeight.Bold,fontSize=13.sp)
     Spacer(Modifier.weight(1f)); Text("${filtered.size} tools",color=lilac,fontSize=10.sp)
    }
    Spacer(Modifier.height(8.dp))
    filtered.chunked(2).forEach { row ->
     Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
      row.forEach { item ->
       Box(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(.09f)).padding(11.dp)) {
        Text(item,color=Color.White,fontSize=11.sp)
       }
      }
      if(row.size==1) Spacer(Modifier.weight(1f))
     }
     Spacer(Modifier.height(6.dp))
    }
   }
  }
  Panel {
   Text("AI WORKSPACE",color=Color.White,fontWeight=FontWeight.SemiBold)
   Text("Connect your own secure AI backend to enable live assistant responses.",color=lilac,fontSize=11.sp)
   Action("Open U SHORT AI",openAI,true)
  }
 }
}

@Composable private fun AssistantScreen() {
 val context= LocalContext.current
 val prefs=context.getSharedPreferences("ushort_settings",Context.MODE_PRIVATE)
 var endpoint by remember { mutableStateOf(prefs.getString("ai_endpoint","") ?: "") }
 var message by remember { mutableStateOf("") }
 var reply by remember { mutableStateOf("") }
 var busy by remember { mutableStateOf(false) }
 var error by remember { mutableStateOf("") }
 val scope= rememberCoroutineScope()
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Heading("U SHORT AI","Your creative copilot, inside the app.")
  Panel {
   Row(verticalAlignment=Alignment.CenterVertically) {
    Box(Modifier.size(42.dp).clip(CircleShape).background(Brush.linearGradient(listOf(pink,cyan))),contentAlignment=Alignment.Center) {
     Icon(Icons.Default.AutoAwesome,null,tint=Color.White,modifier=Modifier.size(24.dp))
    }
    Spacer(Modifier.width(10.dp))
    Column {
     Text("U Assistant",color=Color.White,fontWeight=FontWeight.SemiBold)
     Text(if(endpoint.isBlank()) "AI connection not configured" else "AI endpoint saved",color=lilac,fontSize=10.sp)
    }
   }
   Spacer(Modifier.height(12.dp))
   Text("AI server endpoint",color=Color.White,fontSize=12.sp)
   OutlinedTextField(endpoint,{endpoint=it},modifier=Modifier.fillMaxWidth(),singleLine=true,placeholder={Text("https://your-server.example/chat")})
   Text("Use your own HTTPS backend. Expected API: POST JSON { message: string }, response JSON { reply: string }. Keep provider API keys on the server, never in the APK.",color=lilac,fontSize=10.sp)
   Action("Save AI connection",{ prefs.edit().putString("ai_endpoint",endpoint.trim()).apply(); error="AI endpoint saved on this device." },false)
  }
  Panel {
   Text("Ask U SHORT AI",color=Color.White,fontWeight=FontWeight.SemiBold)
   if(reply.isNotBlank()) {
    Text("ASSISTANT",color=cyan,fontSize=9.sp,fontWeight=FontWeight.Bold)
    Text(reply,color=Color.White,fontSize=13.sp)
   } else Text("Ask for video ideas, scripts, hooks, captions, or editing guidance.",color=lilac,fontSize=12.sp)
   if(error.isNotBlank()) Text(error,color=if(error.contains("saved")) Color(0xFFB9FFD9) else Color(0xFFFFB5C1),fontSize=11.sp)
   OutlinedTextField(message,{message=it},modifier=Modifier.fillMaxWidth(),placeholder={Text("Write your request…")},minLines=2)
   Button(
    onClick={
     val prompt=message.trim()
     val url=endpoint.trim()
     if(prompt.isEmpty()) error="Write a message first."
     else if(url.isEmpty()) error="Add and save your AI backend endpoint first."
     else {
      busy=true; error=""; reply=""
      scope.launch {
       val result=withContext(Dispatchers.IO) { requestAssistant(url,prompt) }
       result.fold(
        onSuccess={reply=it; message=""},
        onFailure={error=it.message ?: "AI request failed. Check the endpoint and try again."}
       )
       busy=false
      }
     }
    },
    enabled=!busy,
    modifier=Modifier.fillMaxWidth().height(48.dp),
    shape=RoundedCornerShape(16.dp),
    colors=ButtonDefaults.buttonColors(containerColor=Color.White,contentColor=ink)
   ) {
    if(busy) { CircularProgressIndicator(modifier=Modifier.size(18.dp),strokeWidth=2.dp,color=ink); Spacer(Modifier.width(8.dp)); Text("Connecting…") }
    else { Icon(Icons.Default.Send,null); Spacer(Modifier.width(8.dp)); Text("Send to AI") }
   }
  }
 }
}

private fun requestAssistant(endpoint:String,prompt:String):Result<String> = runCatching {
 val connection= URL(endpoint).openConnection() as HttpURLConnection
 try {
  connection.requestMethod="POST"
  connection.connectTimeout=15000
  connection.readTimeout=45000
  connection.doOutput=true
  connection.setRequestProperty("Content-Type","application/json; charset=utf-8")
  connection.setRequestProperty("Accept","application/json")
  val body=JSONObject().put("message",prompt).toString()
  connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
  val code=connection.responseCode
  val stream=if(code in 200..299) connection.inputStream else connection.errorStream
  val response=stream?.bufferedReader()?.use { it.readText() }.orEmpty()
  if(code !in 200..299) throw IllegalStateException("AI server returned HTTP $code: ${response.take(240)}")
  val parsed=JSONObject(response)
  parsed.optString("reply").takeIf { it.isNotBlank() }
   ?: throw IllegalStateException("AI response must contain a non-empty 'reply' field.")
 } finally { connection.disconnect() }
}

@Composable private fun Home(auto:()->Unit,manual:()->Unit,pro:()->Unit) {
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(15.dp)) {
  Text("Create. Edit. Grow.",color=lilac,fontSize=13.sp)
  Text("Make every\\nsecond count.",color=Color.White,fontSize=31.sp,lineHeight=35.sp,fontWeight=FontWeight.SemiBold)
  Panel {
   Preview()
   Spacer(Modifier.height(13.dp)); Action("＋  Create Video",auto,true)
  }
  Text("Choose your workflow",color=Color.White,fontSize=17.sp,fontWeight=FontWeight.SemiBold)
  Mode("Auto Shorts","Turn long videos into short clips",Icons.Default.Bolt,auto)
  Mode("Manual Shorts","Pick scenes and shape your story",Icons.Default.ContentCut,manual)
  Mode("Pro Studio","Timeline, color and audio tools",Icons.Default.Tune,pro)
 }
}
@Composable private fun Auto(video:Uri?,pick:()->Unit) {
 var url by remember { mutableStateOf("") }; var duration by remember { mutableStateOf("30 sec") }; var status by remember { mutableStateOf("") }
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(13.dp)) {
  Heading("Auto Shorts","AI finds the strongest moments.")
  Panel {
   Text("Video source",color=Color.White,fontWeight=FontWeight.SemiBold)
   OutlinedTextField(url,{url=it},modifier=Modifier.fillMaxWidth(),placeholder={Text("Paste video link")},singleLine=true)
   Action("＋  Choose video from phone",pick,false)
   if(video!=null) Text("Video selected ✓",color=Color(0xFFB9FFD9))
  }
  Panel {
   Text("Duration",color=Color.White); Choices(listOf("15 sec","30 sec","60 sec"),duration){duration=it}
   Spacer(Modifier.height(10.dp)); Text("AI steps",color=Color.White)
   listOf("Scene detection","Hook selection","Smart captions","Clip suggestions").forEach { Text("✦  $it",color=lilac,fontSize=13.sp) }
  }
  Action("✦  Analyze video",{status=if(video!=null||url.isNotBlank()) "Source selected. AI backend connection is the next development stage." else "Choose a video or paste a link first."},true)
  Text(status,color=Color.White,fontSize=12.sp)
 }
}
@Composable private fun Manual(video:Uri?,pick:()->Unit) {
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(13.dp)) {
  Heading("Manual Shorts","You choose. AI assists.")
  Panel { Preview(video); Action("＋  Import video",pick,false); if(video!=null) Text("Video selected ✓",color=Color(0xFFB9FFD9)) }
  Panel { Text("Quick tools",color=Color.White,fontWeight=FontWeight.SemiBold); ToolGrid(listOf("Trim","Split","Reorder","Captions","Music","Hook")) }
  Action("Create timeline",{},true)
  Text("Timeline engine will be connected in the next stage.",color=lilac,fontSize=12.sp)
 }
}
@Composable private fun Pro(video:Uri?,pick:()->Unit) {
 var exposure by remember { mutableFloatStateOf(0f) }; var contrast by remember { mutableFloatStateOf(0f) }; var saturation by remember { mutableFloatStateOf(0f) }
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Heading("Pro Studio","Professional editing workspace.")
  Panel { Preview(video); Action("＋  Import footage",pick,false); if(video!=null) Text("Video selected ✓",color=Color(0xFFB9FFD9)) }
  Panel { Text("Editing tools",color=Color.White,fontWeight=FontWeight.SemiBold); ToolGrid(listOf("Trim","Split","Speed","Effects","Text","Audio","Mask","Keyframe")) }
  Panel {
   Text("U SHORT Color Lab",color=Color.White,fontWeight=FontWeight.SemiBold)
   SliderLine("Exposure",exposure){exposure=it}; SliderLine("Contrast",contrast){contrast=it}; SliderLine("Saturation",saturation){saturation=it}
   Text("Lift • Gamma • Gain • Curves • HSL • LUT",color=lilac,fontSize=11.sp)
  }
  Action("Export",{},true)
  Text("Rendering and grading engines are not connected yet.",color=lilac,fontSize=11.sp)
 }
}
@Composable private fun Profile() {
 Column(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Heading("Creator profile","Your workspace.")
  Panel { listOf("Connected accounts","Cloud library","Publishing calendar","Analytics","Settings").forEach { Row(Modifier.fillMaxWidth().padding(13.dp),horizontalArrangement=Arrangement.SpaceBetween){Text(it,color=Color.White);Icon(Icons.Default.ChevronRight,null,tint=lilac)}; Divider(color=Color.White.copy(.15f)) } }
 }
}
@Composable private fun Heading(a:String,b:String) { Column { Text(a,color=Color.White,fontSize=25.sp,fontWeight=FontWeight.SemiBold); Text(b,color=lilac,fontSize=12.sp) } }
@Composable private fun Panel(content:@Composable ColumnScope.()->Unit) {
 Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(23.dp)).background(Brush.linearGradient(listOf(Color(0x44FFFFFF),Color(0x1CFFFFFF),Color(0x22B77BEB)))).border(1.dp,Color.White.copy(.25f),RoundedCornerShape(23.dp)).padding(15.dp),content=content)
}
@Composable private fun Preview(uri: Uri? = null) {
 if (uri != null) {
  val context = androidx.compose.ui.platform.LocalContext.current
  val player = remember(uri) {
   ExoPlayer.Builder(context).build().apply {
    setMediaItem(MediaItem.fromUri(uri))
    prepare()
    playWhenReady = false
   }
  }
  DisposableEffect(player) { onDispose { player.release() } }
  AndroidView(
   factory = { ctx -> PlayerView(ctx).apply {
    this.player = player
    useController = true
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
   } },
   update = { it.player = player },
   modifier = Modifier.fillMaxWidth().height(210.dp).clip(RoundedCornerShape(18.dp))
  )
 } else {
  Box(Modifier.fillMaxWidth().height(170.dp).clip(RoundedCornerShape(18.dp)).background(Brush.linearGradient(listOf(Color(0xFF13A9DD),Color(0xFF7646C6),Color(0xFFE77BCB)))),contentAlignment=Alignment.Center) {
   Icon(Icons.Default.PlayCircleFilled,null,tint=Color.White,modifier=Modifier.size(62.dp))
   Text("IMPORT A VIDEO TO PREVIEW",Modifier.align(Alignment.BottomStart).padding(12.dp),color=Color.White,fontSize=10.sp)
  }
 }
}
@Composable private fun Mode(title:String,sub:String,icon:androidx.compose.ui.graphics.vector.ImageVector,click:()->Unit) {
 Panel { Row(Modifier.fillMaxWidth().clickable { click() },verticalAlignment=Alignment.CenterVertically) {
  Box(Modifier.size(45.dp).clip(CircleShape).background(Brush.linearGradient(listOf(pink,Color(0xFF7334C8)))),contentAlignment=Alignment.Center){Icon(icon,null,tint=Color.White)}
  Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)){Text(title,color=Color.White,fontWeight=FontWeight.SemiBold);Text(sub,color=lilac,fontSize=11.sp)}
  Icon(Icons.Default.ArrowForward,null,tint=Color.White)
 } }
}
@Composable private fun Action(label:String,click:()->Unit,primary:Boolean) {
 if(primary) Button(onClick=click,modifier=Modifier.fillMaxWidth().height(50.dp),shape=RoundedCornerShape(17.dp),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFF8F3FF),contentColor=ink)){Text(label,fontWeight=FontWeight.SemiBold)}
 else OutlinedButton(onClick=click,modifier=Modifier.fillMaxWidth().height(47.dp),shape=RoundedCornerShape(16.dp),colors=ButtonDefaults.outlinedButtonColors(contentColor=Color.White)){Text(label)}
}
@Composable private fun Choices(items:List<String>,selected:String,onPick:(String)->Unit) {
 Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)){items.forEach { Box(Modifier.clip(CircleShape).background(if(it==selected) Color.White else Color.White.copy(.12f)).clickable { onPick(it) }.padding(horizontal=13.dp,vertical=9.dp)){Text(it,color=if(it==selected) ink else Color.White,fontSize=11.sp)} }}
}
@Composable private fun ToolGrid(items:List<String>) {
 items.chunked(4).forEach { row -> Row(horizontalArrangement=Arrangement.spacedBy(7.dp)){row.forEach { Box(Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).background(Color.White.copy(.11f)).padding(vertical=13.dp),contentAlignment=Alignment.Center){Text(it,color=Color.White,fontSize=10.sp)} }; repeat(4-row.size){Spacer(Modifier.weight(1f))} }; Spacer(Modifier.height(7.dp)) }
}
@Composable private fun SliderLine(label:String,value:Float,onChange:(Float)->Unit) {
 Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(label,color=lilac,fontSize=12.sp);Text("%.2f".format(value),color=Color.White,fontSize=11.sp)}
 Slider(value,onValueChange=onChange,valueRange=-1f..1f,colors=SliderDefaults.colors(thumbColor=Color.White,activeTrackColor=pink,inactiveTrackColor=Color.White.copy(.2f)))
}
