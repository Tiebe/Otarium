package nl.tiebe.otarium.utils.otariumicons.bottombar

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.otariumicons.BottombarGroup
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box10Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box10Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box1Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box1Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box2Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box2Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box3Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box3Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box4Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box4Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box5Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box5Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box6Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box6Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box7Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box7Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box8Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box8Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box9Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.Box9Outline
import kotlin.collections.List as ____KtList

public object GradesGroup

public val BottombarGroup.Grades: GradesGroup
  get() = GradesGroup

private var __AllAssets: ____KtList<ImageVector>? = null

public val GradesGroup.AllAssets: ____KtList<ImageVector>
  get() {
    if (__AllAssets != null) {
      return __AllAssets!!
    }
    __AllAssets= listOf(Box10Filled, Box10Outline, Box1Filled, Box1Outline, Box2Filled, Box2Outline,
        Box3Filled, Box3Outline, Box4Filled, Box4Outline, Box5Filled, Box5Outline, Box6Filled,
        Box6Outline, Box7Filled, Box7Outline, Box8Filled, Box8Outline, Box9Filled, Box9Outline)
    return __AllAssets!!
  }
