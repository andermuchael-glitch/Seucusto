package br.com.seucusto.app.domain

import java.math.BigDecimal
import java.math.RoundingMode

private const val CALCULATION_SCALE = 8

data class CostBreakdown(
    val material: BigDecimal = BigDecimal.ZERO,
    val labor: BigDecimal = BigDecimal.ZERO,
    val fixed: BigDecimal = BigDecimal.ZERO,
    val variable: BigDecimal = BigDecimal.ZERO,
    val packaging: BigDecimal = BigDecimal.ZERO,
    val waste: BigDecimal = BigDecimal.ZERO,
    val other: BigDecimal = BigDecimal.ZERO
) {
    val total: BigDecimal
        get() = material + labor + fixed + variable + packaging + waste + other
}

object CostCalculationService {
    fun calculateMaterialCost(quantity: BigDecimal, unitCost: BigDecimal): BigDecimal =
        quantity * unitCost

    fun calculateLaborCost(
        timeHours: BigDecimal,
        hourlyValue: BigDecimal,
        workers: BigDecimal
    ): BigDecimal = timeHours * hourlyValue * workers

    fun calculateFixedCost(
        monthly: BigDecimal,
        estimatedProduction: BigDecimal
    ): BigDecimal =
        if (estimatedProduction.signum() == 0) {
            BigDecimal.ZERO
        } else {
            monthly.divide(estimatedProduction, CALCULATION_SCALE, RoundingMode.HALF_UP)
        }

    fun calculateWaste(base: BigDecimal, percent: BigDecimal): BigDecimal =
        base.multiply(percent)
            .divide(BigDecimal(100), CALCULATION_SCALE, RoundingMode.HALF_UP)

    fun calculateVariableFromCost(base: BigDecimal, percent: BigDecimal): BigDecimal =
        base.multiply(percent)
            .divide(BigDecimal(100), CALCULATION_SCALE, RoundingMode.HALF_UP)

    fun calculateVariableFromSale(price: BigDecimal, percent: BigDecimal): BigDecimal =
        price.multiply(percent)
            .divide(BigDecimal(100), CALCULATION_SCALE, RoundingMode.HALF_UP)

    fun calculateTotalCost(breakdown: CostBreakdown): BigDecimal =
        breakdown.total

    fun calculateSellingPrice(
        cost: BigDecimal,
        marginPercent: BigDecimal
    ): BigDecimal {
        require(marginPercent >= BigDecimal.ZERO && marginPercent < BigDecimal(100))
        val marginFraction = marginPercent.divide(
            BigDecimal(100),
            CALCULATION_SCALE,
            RoundingMode.HALF_UP
        )
        val denominator = BigDecimal.ONE.subtract(marginFraction)
        return cost.divide(denominator, CALCULATION_SCALE, RoundingMode.HALF_UP)
    }

    fun calculateMargin(cost: BigDecimal, price: BigDecimal): BigDecimal =
        if (price.signum() == 0) {
            BigDecimal.ZERO
        } else {
            price.subtract(cost)
                .multiply(BigDecimal(100))
                .divide(price, CALCULATION_SCALE, RoundingMode.HALF_UP)
        }

    fun calculateMarkup(cost: BigDecimal, price: BigDecimal): BigDecimal =
        if (cost.signum() == 0) {
            BigDecimal.ZERO
        } else {
            price.divide(cost, CALCULATION_SCALE, RoundingMode.HALF_UP)
        }
}
