package com.lcb.one.ui.screen.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun EventCard(modifier: Modifier = Modifier, title: String, content: String, icon: Any? = null) {
    Card(modifier = modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (titleRef, contentRef, iconRef) = createRefs()
            Text(
                modifier = Modifier.constrainAs(titleRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(modifier = Modifier
                .constrainAs(contentRef) {
                    start.linkTo(parent.start)
                    top.linkTo(titleRef.bottom)
                }
                .padding(top = 4.dp),
                text = content,
                style = MaterialTheme.typography.titleSmall
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(icon)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .constrainAs(iconRef) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                    }
                    .size(32.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        }
    }
}