package ir.sharif.simplenote.ui.features.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Envelope
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.PencilSquare
import ir.sharif.simplenote.R
import ir.sharif.simplenote.ui.theme.ColorPalette
import ir.sharif.simplenote.ui.theme.TextStyles

@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    userName: String,
    userEmail: String,
    onEditProfileClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(64.dp)
            ) {
                Text(
                    text = userName,
                    style = TextStyles.textLgBold,
                    color = ColorPalette.NeutralBlack
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
                        text = userEmail,
                        style = TextStyles.text2Xs,
                        color = ColorPalette.NeutralDarkGrey
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(1.dp, ColorPalette.PrimaryBase),
                    RoundedCornerShape(100.dp)
                )
                .clickable(onClick = onEditProfileClick)
                .padding(horizontal = 16.dp, vertical = 8.dp)
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