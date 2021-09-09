package com.vengateshm.android.composecalculators

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vengateshm.android.composecalculators.ui.theme.ComposeCalculatorsTheme
import com.vengateshm.android.composecalculators.ui.theme.Purple700
import com.vengateshm.android.composecalculators.ui.theme.Teal200

// Rupee symbol - ₹ \u20B9
// Rs. - \u20A8

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCalculatorsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    FDCalculatorState()
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun FDCalculatorState() {
    val totalInvestment = remember { mutableStateOf("1000") }
    val rateOfInterest = remember { mutableStateOf("1") }
    val timePeriod = remember { mutableStateOf("1") }
    val totalInterest = remember { mutableStateOf("10") }
    val maturityValue = remember { mutableStateOf("1010") }
    val keyboardController = LocalSoftwareKeyboardController.current

    FDCalculator(totalInvestment = totalInvestment.value,
        rateOfInterest = rateOfInterest.value,
        timePeriod = timePeriod.value,
        onTotalInvestmentChanged = {
            totalInvestment.value = it
            if (it.isNotEmpty()) {
                val (totalInterestAmt, maturity) = calculateResult(totalInvestment.value.toDouble(),
                    rateOfInterest.value.toDouble(),
                    timePeriod.value.toInt())
                totalInterest.value = totalInterestAmt.toString()
                maturityValue.value = maturity.toString()
            }
        },
        onRateOfInterestChanged = {
            rateOfInterest.value = it
            if (it.isNotEmpty()) {
                val (totalInterestAmt, maturity) = calculateResult(totalInvestment.value.toDouble(),
                    rateOfInterest.value.toDouble(),
                    timePeriod.value.toInt())
                totalInterest.value = totalInterestAmt.toString()
                maturityValue.value = maturity.toString()
            }
        },
        onTimePeriodChanged = {
            timePeriod.value = it
            if (it.isNotEmpty()) {
                val (totalInterestAmt, maturity) = calculateResult(totalInvestment.value.toDouble(),
                    rateOfInterest.value.toDouble(),
                    timePeriod.value.toInt())
                totalInterest.value = totalInterestAmt.toString()
                maturityValue.value = maturity.toString()
            }
        },
        totalInterest = totalInterest.value,
        maturityValue = maturityValue.value,
        onKeyBoardDonePressed = {
            keyboardController?.hide()
        })
}

@Composable
fun FDCalculator(
    totalInvestment: String,
    rateOfInterest: String,
    timePeriod: String,
    onTotalInvestmentChanged: (String) -> Unit,
    onRateOfInterestChanged: (String) -> Unit,
    onTimePeriodChanged: (String) -> Unit,
    totalInterest: String,
    maturityValue: String,
    onKeyBoardDonePressed: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        TextField(value = totalInvestment, onValueChange = {
            onTotalInvestmentChanged(it)
        }, singleLine = true, label = { Text(text = "Total Investment") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onKeyBoardDonePressed.invoke()
            }))
        Spacer(modifier = Modifier.height(15.dp))
        TextField(value = rateOfInterest, onValueChange = {
            onRateOfInterestChanged(it)
        }, singleLine = true, label = { Text(text = "Rate of Interest in % (P.A)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = {
                onKeyBoardDonePressed.invoke()
            }))
        Spacer(modifier = Modifier.height(15.dp))
        TextField(value = timePeriod, onValueChange = {
            onTimePeriodChanged(it)
        }, singleLine = true, label = { Text(text = "Time Period in Years") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = {
                onKeyBoardDonePressed.invoke()
            }))

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Total Investment",
            style = MaterialTheme.typography.body1)
        Text(text = "₹$totalInvestment")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Total Interest",
            style = MaterialTheme.typography.body1)
        Text(text = "₹$totalInterest")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Maturity Value",
            style = MaterialTheme.typography.body1)
        Text(text = "₹$maturityValue")

        Spacer(modifier = Modifier.height(20.dp))

        if (totalInvestment.isNotEmpty()) {
            Canvas(modifier = Modifier.size(100.dp)) {
                val totalInvestmentSweepAngle =
                    ((totalInvestment.toDouble() / maturityValue.toDouble()) * 360.0).toFloat()
                val totalInterestSweepAngle =
                    ((totalInterest.toDouble() / maturityValue.toDouble()) * 360.0).toFloat()

                drawArc(Color(0XFF5367FF), useCenter = false,
                    startAngle = 270f,
                    sweepAngle = totalInvestmentSweepAngle,
                    style = Stroke(width = 24.dp.toPx()))
                drawArc(Color(0XFF00D09C), useCenter = false,
                    startAngle = 270f - totalInterestSweepAngle,
                    sweepAngle = totalInterestSweepAngle,
                    style = Stroke(width = 24.dp.toPx()))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.wrapContentSize()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(modifier = Modifier
                        .size(20.dp, 10.dp)
                        .background(color = Color(0XFF5367FF)),
                        text = "")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Total Investment")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(modifier = Modifier
                        .size(20.dp, 10.dp)
                        .background(color = Color(0XFF00D09C)),
                        text = "")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Total Interest")
                }
            }
        }
    }
}

fun calculateResult(
    totalInvestment: Double,
    rateOfInterest: Double,
    timePeriod: Int,
): Pair<Double, Double> {
    val totalInterest = totalInvestment * ((rateOfInterest / 100f) * timePeriod.toDouble())
    val maturity = totalInvestment + totalInterest
    return Pair(totalInterest, maturity)
}