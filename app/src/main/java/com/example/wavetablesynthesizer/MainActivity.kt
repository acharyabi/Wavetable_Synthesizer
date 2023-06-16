package com.example.wavetablesynthesizer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.NumberPicker.OnValueChangeListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.wavetablesynthesizer.ui.theme.WavetableSynthesizerTheme
import com.example.wavetablesynthesizer.ui.theme.orange

class MainActivity : ComponentActivity() {
    private val synthesizerViewModel: WavetableSynthesizerViewModel by viewModels()
    private val synthesizer = LoggingWavetableSynthesizer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        synthesizerViewModel.wavetableSynthesizer = synthesizer
        setContent {
            WavetableSynthesizerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WavetableSynthesizerApp(Modifier, synthesizerViewModel)
                }
            }
        }
    }
    override fun OnResume() {
        super.onResume()
        synthesizerViewModel.applyParameters()
    }

}

@Composable
fun WavetableSynthesizerApp(
    modifier:Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Text("Our First Column")
        //Chooese the wavestable that we want to play
        WavetableSelectionPanel(modifier, synthesizerViewModel)
        //Control How The Synthesizer Behaves
        ControlsPanel(modifier, synthesizerViewModel)
    }
}

@Composable
fun WavetableSelectionPanel(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
){
    Row (modifier= modifier
        .fillMaxWidth()
        .fillMaxHeight(0.5f)
        .border(BorderStroke(5.dp, Color.Black)),
    //Colums have vertical arrangement and horizontal alignment and rows have exactly opposite.
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier=modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.waveselection))
            WavetableSelectionButton(modifier, synthesizerViewModel)
        }
        }

}

@Composable
fun WavetableSelectionButton(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
){
    Row(
        modifier=modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly

    ){
        for (wavetable in Wavetable.values()){
            WavetableButton(
                modifier=   modifier,
                onClick =   {synthesizerViewModel.setWavetable(wavetable)},
                label   =   stringResource(wavetable.toResourceString()) ,
            )
        }
    }
}
@Composable
fun WavetableButton(
    modifier: Modifier,
    //It takes no arguments and returns nothing.
    onClick:() ->Unit,
    label:String
){
    Button( modifier=modifier,
            onClick=onClick,
        ){
            Text(text = label)
    }
}
@Composable
fun ControlsPanel(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
){
    Row(modifier= modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .border(BorderStroke(5.dp, Color.Black)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier= modifier
                .fillMaxHeight()
                .fillMaxWidth(0.7f)
                .border(BorderStroke(5.dp, Color.Black)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PitchControl(modifier, synthesizerViewModel)
            PlayControl(modifier, synthesizerViewModel)
        }
        Column (
            modifier= modifier
                .fillMaxSize()
                .border(BorderStroke(5.dp, Color.Black)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
                ){
                VolumeControl(modifier, synthesizerViewModel)
        }
    }
}

//Using state and its hoist is called state hoisting in jetpack compose.
@Composable
fun PitchControl(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {

    /*//rememberSavable is used because it Also Works During Time of reconfiguration
    var frequnecy = rememberSaveable {
        mutableStateOf(300F)
    }*/
    val frequnecy= synthesizerViewModel.frequency.observeAsState()

    /*//It has already been apllied below in the form of the content.
    Slider(modifier = modifier, value = frequnecy.value, onValueChange = {
        frequnecy.value = it
    }, valueRange = 40F..3000F)

     */
    PitchControlContent(
        modifier,
        pitchControlLabel = stringResource(R.string.frequency),
        value = frequnecy.value!!,
        onValueChange = {
            synthesizerViewModel.setFrequencySliderPosition(it)
        },
        valueRange = 0f..1f,
        frequencyValueLabel = stringResource(R.string.frequency_value, frequnecy.value!!)
    )
}

@Composable
fun PitchControlContent(
    modifier: Modifier,
    pitchControlLabel:String,
    value: Float,
    //This passes nothing right here
    onValueChange: (Float)->Unit,
    valueRange:ClosedFloatingPointRange<Float>,
    frequencyValueLabel:String
){
    Text(pitchControlLabel)
    Slider(modifier = modifier, value= value , onValueChange= onValueChange, valueRange=40F..3000F )
    Text(frequencyValueLabel)
}
@Composable
fun PlayControl(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
){
    val playButtonLabel = synthesizerViewModel.playButtonLabel.observeAsState()
   Button(
       modifier= modifier,
       onClick = {synthesizerViewModel.playClicked()}
   ) {
       Text(stringResource(playButtonLabel.value!!))
       
   }
}

@Composable
fun VolumeControl(
    modifier:Modifier,
    //Initializing View Model.
    synthesizerViewModel: WavetableSynthesizerViewModel
){
    /*val volume = rememberSaveable{
        mutableStateOf(-10F)
    }*/
    //Now we want to see the changes that occur in the ViewModel
    val volume = synthesizerViewModel.volume.observeAsState()

    VolumeControlContent(
        modifier=modifier,
        value=volume.value!!,
        onValueChange={
            //volume.value=it
            synthesizerViewModel.setVolume(it)
        },
        //volumeRange = -60F..0F
        volumeRange = synthesizerViewModel.volumeRange
    )

}
@Composable
fun VolumeControlContent(
    modifier: Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    volumeRange: ClosedFloatingPointRange<Float>

){
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sliderHeight = screenHeight / 4

    Icon(imageVector = Icons.Filled.VolumeUp, contentDescription =null )
    Slider(
        value = value,
        //Changing with respect to the change in the slider.
        onValueChange = onValueChange,
        modifier= modifier
            .width(sliderHeight.dp)
            .rotate(270f) ,
        valueRange = volumeRange
    )
    Icon(imageVector = Icons.Filled.VolumeMute, contentDescription =null )
}

//We can remove the boarder to make the user interface look more intuitive.