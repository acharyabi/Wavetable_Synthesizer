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
import androidx.compose.foundation.layout.offset
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
    private val synthesizer = NativeWaveTableSynthesizer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        //This ensures onResume and onCall will be called as they are being called in the MainActivity.kt.
        lifecycle.addObserver(synthesizer)

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
    override fun onDestroy(){
        super.onDestroy()

        lifecycle.removeObserver(synthesizer)
    }

        override fun onResume() {
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        //Text("Our First Column")
        //Choose the waves-table that we want to play
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
    //Columns have vertical arrangement and horizontal alignment and rows have exactly opposite.
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier=modifier
                .fillMaxWidth()
                .fillMaxHeight(),
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
    var frequency = rememberSavable {
        mutableStateOf(300F)
    }*/
    val frequency= synthesizerViewModel.frequency.observeAsState()

    /*//It has already been applied below in the form of the content.
    Slider(modifier = modifier, value = frequency.value, onValueChange = {
        frequency.value = it
    }, valueRange = 40F..3000F)

    // the slider position state is hoisted by this composable; no need to embed it into
  // the ViewModel, which ideally, shouldn't be aware of the UI.
  // When the slider position changes, this composable will be recomposed as we explained in the UI tutorial.

     */
    val sliderPosition = rememberSaveable {
        mutableStateOf(
            // we use the ViewModel's convenience function to get the initial slider position
            synthesizerViewModel.sliderPositionFromFrequencyInHz(frequency.value!!)
        )
    }
    PitchControlContent(
        modifier=modifier,
        pitchControlLabel = stringResource(R.string.frequency),
        value = sliderPosition.value,
        onValueChange = {
            sliderPosition.value = it
            synthesizerViewModel.setFrequencySliderPosition(it)
        },
        valueRange = 0F..1F,
        frequencyValueLabel = stringResource(R.string.frequency_value, frequency.value!!)
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
    Slider(modifier = modifier, value= value , onValueChange= onValueChange, valueRange=valueRange )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(modifier = modifier, text = frequencyValueLabel)
    }
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
    /*val volume = rememberSavable{
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

) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sliderHeight = screenHeight / 4

    Icon(imageVector = Icons.Filled.VolumeUp, contentDescription = null)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .offset(y = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Slider(
            value = value,
            //Changing with respect to the change in the slider.
            onValueChange = onValueChange,
            modifier = modifier
                .width(sliderHeight.dp)
                .rotate(270f),
            valueRange = volumeRange
        )
        Icon(imageVector = Icons.Filled.VolumeMute, contentDescription = null)
    }
}

//We can remove the boarder to make the user interface look more intuitive.