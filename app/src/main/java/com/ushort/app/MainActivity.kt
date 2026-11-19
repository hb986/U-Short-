package com.ushort.app

import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
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

private val ink = Color(0xFF21123E)
private val lilac = Color(0xFFDCCBFF)
private val pink = Color(0xFFD66BCB)
private val cyan = Color(0xFF49C9F0)

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { App() } }
}
private enum class Tab(val title:String) { HOME("Home"), AUTO("Auto"), MANUAL("Manual"), PRO("Pro"), PROFILE("Profile") }

@Composable fun App() {
 var tab by remember { mutableStateOf(Tab.HOME) }
 var video by remember { mutableStateOf<Uri?>(null) }
 val pick = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { video = it }
 Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF271044),Color(0xFF7435A8),Color(0xFFB75BAE),Color(0xFF253F91))))) {
  Column(Modifier.fillMaxSize()) {
   Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment=Alignment.CenterVertically) {
    Box(Modifier.size(38.dp).clip(RoundedCornerShape(13.dp)).background(Brush.linearGradient(listOf(pink,Color(0xFF7839D1),cyan))), contentAlignment=Alignment.Center) { Icon(Icons.Default.PlayArrow,null,tint=Color.White) }
    Spacer(Modifier.width(10.dp)); Text("U SHORT",color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold)
    Spacer(Modifier.weight(1f)); Icon(Icons.Default.NotificationsNone,null,tint=Color.White)
   }
   Box(Modifier.weight(1f)) {
    when(tab) {
     Tab.HOME -> Home({tab=Tab.AUTO},{tab=Tab.MANUAL},{tab=Tab.PRO})
     Tab.AUTO -> Auto(video) { pick.launch(arrayOf("video/*")) }
     Tab.MANUAL -> Manual(video) { pick.launch(arrayOf("video/*")) }
     Tab.PRO -> Pro(video) { pick.launch(arrayOf("video/*")) }
     Tab.PROFILE -> Profile()
    }
   }
   Row(Modifier.fillMaxWidth().padding(9.dp).clip(RoundedCornerShape(24.dp)).background(Color(0x5530195B)).border(1.dp,Color.White.copy(.2f),RoundedCornerShape(24.dp)).padding(vertical=9.dp),horizontalArrangement=Arrangement.SpaceAround) {
    listOf(Tab.HOME to Icons.Default.Home,Tab.AUTO to Icons.Default.Bolt,Tab.MANUAL to Icons.Default.ContentCut,Tab.PRO to Icons.Default.Tune,Tab.PROFILE to Icons.Default.Person).forEach { (t,ic) ->
     Column(Modifier.clickable { tab=t },horizontalAlignment=Alignment.CenterHorizontally) {
      Icon(ic,null,tint=if(tab==t) Color.White else lilac.copy(.7f),modifier=Modifier.size(20.dp))
      Text(t.title,color=if(tab==t) Color.White else lilac.copy(.7f),fontSize=9.sp)
     }
    }
   }
  }
 }
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
  Panel { Preview(); Action("＋  Import footage",pick,false); if(video!=null) Text("Video selected ✓",color=Color(0xFFB9FFD9)) }
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
