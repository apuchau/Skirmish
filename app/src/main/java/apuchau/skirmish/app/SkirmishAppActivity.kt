package apuchau.skirmish.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import apuchau.skirmish.army.Army
import apuchau.skirmish.battle.Battle
import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import com.natpryce.onError


class SkirmishAppActivity : Activity() {

	private var battle : Battle? = null
	private var battleView: BattleView? = null

	private val KingArthur = Soldier(SoldierId("King Arthur"))
	private val Mordred = Soldier(SoldierId("Mordred"))

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		createBattle()
		createBattleView()
		displayBattle()
	}

	private fun createBattle() {

		val battlefield = Battlefield(createBattlefieldBoundaries(5,3))

		val armies = setOf(
			createArmy("Arthur's army", setOf(KingArthur)),
			createArmy("Mordred's army", setOf(Mordred))
		)

		val soldiersPositions = SoldiersBattlePositions(listOf(
			Pair(KingArthur, battlefieldPosition(3,2)),
			Pair(Mordred, battlefieldPosition(4,2)))
		)

		battle = Battle.instance(battlefield, armies, soldiersPositions)
			.onError { throw Exception("Can't create battle. ${it.reason}") }
	}

	private fun createBattlefieldBoundaries(width: Int, height: Int): BattlefieldBoundaries {
		return BattlefieldBoundaries.create(width, height)
			.onError { throw Exception("Invalid boundaries. ${it.reason}") }
	}

	fun createBattleView() {

		val rootView : View = getWindow().getDecorView().getRootView()

		val layout = LinearLayout(this)
		layout.layout(0, 0, 0, 0)
		layout.layoutParams = ViewGroup.LayoutParams(rootView.width, rootView.height)
		layout.orientation = LinearLayout.VERTICAL

		battleView = BattleView(this)
		setContentView(battleView)
	}

	fun displayBattle() {

		val battle = this.battle ?: return
		val battleView = this.battleView ?: return

		battleView.displayBattleSnapshot(battle.snapshot())
	}

	fun battlefieldPosition(x: Int, y: Int) =
		BattlefieldPosition.create(x,y)
			.onError { throw Exception("Can't create position. ${it.reason}") }

	fun createArmy(armyId: String, soldiers: Set<Soldier>) =
		Army.create(armyId, soldiers)
			.onError { throw Exception("Can't create army. ${it.reason}") }
}
