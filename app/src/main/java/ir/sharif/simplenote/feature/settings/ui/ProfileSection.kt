package ir.sharif.simplenote.feature.settings.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Envelope
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.PencilSquare
import ir.sharif.simplenote.R
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.TextStyles
import androidx.core.net.toUri

@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    userName: String,
    userEmail: String,
    avatarUri: String?,
    onEditProfileClick: () -> Unit
) {
    val context = LocalContext.current

    // Decode the avatar (if any) once per uri change
    val avatarBitmap = remember(avatarUri) {
        avatarUri?.let {
            try {
                context.contentResolver.openInputStream(it.toUri()).use { stream ->
                    stream?.let { BitmapFactory.decodeStream(it) }
                }
            } catch (_: Throwable) { null }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Avatar (picked bitmap or placeholder)
            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap.asImageBitmap(),
                    contentDescription = "Profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(1.dp, ColorPalette.NeutralLightGrey, CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(1.dp, ColorPalette.NeutralLightGrey, CircleShape)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .weight(1f) // let text wrap nicely and avoid overflow
            ) {
                Text(
                    text = userName.ifBlank { "Your Name" },
                    style = TextStyles.textLgBold,
                    color = ColorPalette.NeutralBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Heroicons.Outline.Envelope,
                        contentDescription = "Email",
                        tint = ColorPalette.NeutralDarkGrey,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = userEmail.ifBlank { "you@example.com" },
                        style = TextStyles.text2Xs,
                        color = ColorPalette.NeutralDarkGrey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Edit Profile button
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = ColorPalette.PrimaryBase,
                    shape = RoundedCornerShape(100.dp)
                )
                .clickable(onClick = onEditProfileClick)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Icon(
                Heroicons.Outline.PencilSquare,
                contentDescription = "Edit Profile",
                tint = ColorPalette.PrimaryBase,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Edit Profile",
                color = ColorPalette.PrimaryBase,
                style = TextStyles.textBaseMedium
            )
        }
    }
}
