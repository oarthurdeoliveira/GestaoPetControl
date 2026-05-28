package com.example.gestaopetcontrol

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestaopetcontrol.banco.Database
import com.example.gestaopetcontrol.databinding.ActivityMainBinding
import androidx.core.graphics.toColorInt

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



        val DarkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val isDarkModeFlag = DarkModeFlags == Configuration.UI_MODE_NIGHT_YES

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

        binding.btnServicos.setOnClickListener {
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

        binding.txtResumoCasa.text = """
            Clientes ativos: ${contar("CLIENTES")}
            Pets ativos: ${contar("PETS")}
            Servicos ativos: ${contar("SERVICOS")}
            Agendamentos ativos: ${contar("AGENDAMENTOS")}
            Receita prevista: R$ %.2f
        """.trimIndent().format(receitaPrevista)
    }
}
