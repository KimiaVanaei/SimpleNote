package ir.sharif.simplenote.feature.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.R
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.TextStyles
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

// ✅ theme-aware container
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

@Composable
fun StartJourney(modifier: Modifier) {
    val context = LocalContext.current

    Surface( // ✅ root uses background, follows system/dynamic theme
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 153.dp, start = 60.dp, end = 60.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.raw.home_figure)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "starting a journey figure",
                    modifier = Modifier
                        .size(240.dp)
                )
                Spacer(modifier = Modifier.size(24.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Start Your Journey",
                        style = TextStyles.textXlBold.copy(color = ColorPalette.NeutralBlack) // ✅ mapped to onSurface
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    Text(
                        "Every big step start with small step.\n" +
                                "Notes your first idea and start\n" +
                                "your journey!",
                        style = TextStyles.textSm.copy(color = ColorPalette.NeutralDarkGrey), // ✅ mapped to onSurfaceVariant
                        textAlign = TextAlign.Center
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(modifier = Modifier.padding(start = 12.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(R.raw.curved_arrow)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .height(100.dp)
                            .size(240.dp)
                    )
                }
            }
        }
    }
}