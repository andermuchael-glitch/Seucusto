package br.com.seucusto.app.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.seucusto.app.data.SeucustoRepository
import kotlinx.coroutines.launch

class RawMaterialViewModel(private val repository: SeucustoRepository) : ViewModel() {
    fun add(
        name: String,
        code: String,
        category: String,
        unit: String,
        minimum: Double,
        costCents: Long,
        supplier: String,
        location: String,
        notes: String
    ) = viewModelScope.launch {
        repository.saveRawMaterial(
            id = null,
            name = name,
            code = code,
            category = category,
            unit = unit,
            minimum = minimum,
            costCents = costCents,
            supplier = supplier,
            location = location,
            notes = notes
        )
    }
}
