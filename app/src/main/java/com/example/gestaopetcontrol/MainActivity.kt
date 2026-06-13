package com.example.gestaopetcontrol

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.databinding.ActivityMainBinding
import androidx.core.graphics.toColorInt
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Database(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.homeIcon.imageTintList = ColorStateList.valueOf(
            "#909090".toColorInt()
        )
        binding.homeText.setTextColor("#909090".toColorInt())







        //Toast.makeText(this, "${isDarkModeFlag}", Toast.LENGTH_SHORT).show()

        binding.clienteArea.setOnClickListener {
            intent = Intent(this, Clientes::class.java)
            startActivity(intent)
        }

        binding.agendamentoArea.setOnClickListener {
            intent = Intent(this, Agendamentos::class.java)
            startActivity(intent)
        }

        binding.petArea.setOnClickListener {
            intent = Intent(this, Pets::class.java)
            startActivity(intent)
        }

        binding.servicosArea.setOnClickListener {
            intent = Intent(this, Servicos::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        carregarRelatorios()
    }

    private fun contar(tabela: String): Int {
        val cursor = database.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM $tabela WHERE APAGADO = 0",
            null
        )
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        cursor.close()
        return total
    }

    private fun carregarRelatorios() {
        val cursorReceita = database.readableDatabase.rawQuery(
            """
            SELECT COALESCE(SUM(SERVICOS.PRECO), 0)
            FROM AGENDAMENTOS
            LEFT JOIN SERVICOS ON AGENDAMENTOS.ID_SERVICO = SERVICOS.ID
            WHERE AGENDAMENTOS.APAGADO = 0
            """.trimIndent(),
            null
        )
        cursorReceita.moveToFirst()
        val receitaPrevista = cursorReceita.getDouble(0)
        cursorReceita.close()

        binding.txtRelaClientes.text = "Clientes ativos: ${contar("CLIENTES")}"
        binding.txtRelaPetsCadastrados.text = "Pets ativos: ${contar("PETS")}"
        binding.txtRelaServicosAtivos.text = "Servicos ativos: ${contar("SERVICOS")}"
        binding.txtRelaAgendamentosAtivos.text = "Agendamentos ativos: ${contar("AGENDAMENTOS")}"
        // O locale FRANCE é so pra trocar o . pelo ,
        // eu poderia so substituir o . pelo ',', mas isso serve também
        binding.txtRelaRenda.text = String.format(Locale.FRANCE, "Receita prevista: R$ %.2f", receitaPrevista)

    }


    private fun aplicarTemaCor() {
        val DarkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeFlag = DarkModeFlags == Configuration.UI_MODE_NIGHT_YES

        if (isDarkModeFlag) {
            binding.main.setBackgroundColor("#111111".toColorInt())

            val texto_cor = "#e4e4e4".toColorInt()
            val icone_nav = "#e4e4e4".toColorInt()

            binding.txtTituloCasa.setTextColor(texto_cor)


            // Layout Relatorios
            binding.layoutLVRelatorios.setBackgroundResource(R.drawable.border_dark)

            binding.txtRelaTitulo.setTextColor(texto_cor)
            binding.txtRelaClientes.setTextColor(texto_cor)
            binding.txtRelaRenda.setTextColor(texto_cor)
            binding.txtRelaServicosAtivos.setTextColor(texto_cor)
            binding.txtRelaPetsCadastrados.setTextColor(texto_cor)
            binding.txtRelaAgendamentosAtivos.setTextColor(texto_cor)

            // Nav
            binding.layoutNav.setBackgroundColor("#1c1c1c".toColorInt())

            binding.agendamentoIcon.imageTintList = ColorStateList.valueOf(
                icone_nav
            )
            binding.agendamentoText.setTextColor(texto_cor)

            binding.petIcon.imageTintList = ColorStateList.valueOf(
                icone_nav
            )
            binding.petsText.setTextColor(texto_cor)

            binding.clienteIcon.imageTintList = ColorStateList.valueOf(
                icone_nav
            )
            binding.clienteText.setTextColor(texto_cor)

            binding.servicoIcon.imageTintList = ColorStateList.valueOf(
                icone_nav
            )
            binding.servicoText.setTextColor(texto_cor)

        }else {

            val texto_cor = "#000000".toColorInt()

            binding.main.setBackgroundColor("#ffffff".toColorInt())

            // Layout Relatorios
            binding.layoutLVRelatorios.setBackgroundResource(R.drawable.border)

            binding.txtRelaTitulo.setTextColor(texto_cor)
            binding.txtRelaClientes.setTextColor(texto_cor)
            binding.txtRelaRenda.setTextColor(texto_cor)
            binding.txtRelaServicosAtivos.setTextColor(texto_cor)
            binding.txtRelaPetsCadastrados.setTextColor(texto_cor)
            binding.txtRelaAgendamentosAtivos.setTextColor(texto_cor)


            // Nav
            binding.layoutNav.setBackgroundColor("#D9D9D9".toColorInt())
        }
    }
}
