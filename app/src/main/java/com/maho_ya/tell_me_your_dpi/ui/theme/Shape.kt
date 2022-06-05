package com.maho_ya.tell_me_your_dpi.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp), // Button、TextField、FloatingActionButton
    medium = RoundedCornerShape(4.dp), // AlertDialog
    large = RoundedCornerShape(8.dp) // ModalDrawer（Navigation Drawer）
)
