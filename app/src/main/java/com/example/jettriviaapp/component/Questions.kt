package com.example.jettriviaapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettriviaapp.model.QuestionItem
import com.example.jettriviaapp.screens.QuestionViewModel

@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

    val questionIndex = remember{
        mutableStateOf(0)
    }

    if (viewModel.data.value.loading == true) {

        CircularProgressIndicator(modifier = Modifier.padding(start = 150.dp, top = 150.dp).fillMaxSize(0.18f)

        )

    } else {
        val question = try{
            questions?.get(questionIndex.value)
        }catch(ex: Exception){
            null
        }
        if (questions != null) {
            QuestionDisplay(question = question!!, questionIndex = questionIndex,
                viewModel = viewModel){
                questionIndex.value = questionIndex.value + 1
            }

        }

    }

}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit = {}
) {
    val choicesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer

        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = Color(0xFF8F93A8)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if(questionIndex.value >= 3) showProgress(score = questionIndex.value)

            QuestionTracker(counter = questionIndex.value, outOf = viewModel.data.value.data?.size!!)
            drawDottedLine(pathEffect)

            Column() {
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.Start)
                        .fillMaxHeight(0.3f),
                    color = Color(0xFF2C2C2C),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )

                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF053E58),
                                        Color(0xFF053E58)
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults
                                .colors(
                                    selectedColor =
                                    if (correctAnswerState.value == true
                                        && index == answerState.value
                                    ) {
                                        Color.Green.copy(alpha = 0.2f)
                                    } else {
                                        Color.Red.copy(alpha = 0.2f)

                                    }
                                )
                        ) // End RadioButton

                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (correctAnswerState.value == true
                                        && index == answerState.value
                                    ) {
                                        Color.Green
                                    } else if (correctAnswerState.value == false
                                        && index == answerState.value
                                    ) {
                                        Color.Red
                                    } else {
                                        Color.White
                                    },
                                    fontSize = 17.sp
                                )
                            ) {

                                append(answerText)

                            }

                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))


                    }
                }
                Button(onClick = { onNextClicked(questionIndex.value) },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF053E58)
                    )) {
                    Text(text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = Color.White,
                        fontSize = 17.sp)

                }


            }

        }

    }
}

@Composable
fun drawDottedLine(pathEffect: PathEffect) {
    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = Color(0xFF000000),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )

    }

}

@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF2C2C2C),
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                ) {
                    append("Question : $counter/")

                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF2C2C2C),
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    ) {
                        append("$outOf")
                    }
                }
            }
        },
        modifier = Modifier.padding(20.dp)
    )

}


@Preview
@Composable
fun showProgress(score: Int = 12){


    val gradient = Brush.linearGradient(listOf(Color.Green, Color.LightGray))

    val progressFactor by remember(score){
        mutableStateOf((score * 0.005f).coerceIn(0f, 1f))
    }


    Row(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF053E58),
                    Color(0xFF053E58)
                )
            ),
            shape = RoundedCornerShape(34.dp)
        )
        .clip(
            RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 50,
                bottomEndPercent = 50,
                bottomStartPercent = 50
            )
        )
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically){

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            // The progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressFactor)
                    .fillMaxHeight()
                    .background(brush = gradient)
                    .clip(RoundedCornerShape(23.dp))
            )

            // The score text in the center
            Text(
                text = score.toString(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(6.dp),
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        }













//        Button(
//            contentPadding = PaddingValues(1.dp),
//            onClick = {  },
//            modifier = Modifier.fillMaxWidth(progressFactor)
//                .background(brush = gradient),
//            enabled = false,
//            elevation = null,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Transparent,
//                disabledContainerColor = Color.Transparent
//            )
//            ) {
//            Text(text = (score * 10).toString(),
//                modifier = Modifier.padding(6.dp)
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.87f)
//                    .clip(shape = RoundedCornerShape(23.dp)),
//                color = Color.White,
//                textAlign = TextAlign.Center)
//
//
//        }


    }

}





