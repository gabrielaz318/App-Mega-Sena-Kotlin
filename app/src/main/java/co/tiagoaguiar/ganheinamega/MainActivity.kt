package co.tiagoaguiar.ganheinamega

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private val keySharedPrefs = "db-megasena"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buscar os objetos e ter a referencia deles
        val editText: EditText = findViewById(R.id.edit_number)
        val txtResult: TextView = findViewById(R.id.txt_result)
        val btnGenerate: Button = findViewById(R.id.btn_generate)

        // Criar a "conexão"
        prefs = getSharedPreferences(keySharedPrefs, Context.MODE_PRIVATE)

        // Recupera a string da chave result, caso não tenha nada retorna null
        val lastResult: String? = prefs.getString("result", null)

        /*
        Substituir isso:

        if(lastResult != null) {
            // Se o resultado for diferente de null ele mostra na tela a última geração
            txtResult.text = "Última aposta: $lastResult"
        }
        */

        // Por isso
            // Verifica se é diferente de null para entrar no bloco certo
        lastResult?.let {
            txtResult.text = "Última aposta: $it"
        }

        // Reconhecer clique no botão
        // opção 1: XML

        // opção 2: variavel que seja do tipo View.OnCLickListener (interface)

            //Forma 1 deixar a função no bloco externo
        //btnGenerate.setOnClickListener(butonClickListener)

            // Forma 2 deixar o bloco da função direto
            //btnGenerate.setOnClickListener(View.OnClickListener {
            //  Log.i("Teste", "Botão clicado")
            //})

        // opção 3: bloco de código que será disparado pelo onClickListener

        btnGenerate.setOnClickListener(View.OnClickListener {
            val text = editText.text.toString()
            numberGenerator(text, txtResult)
        })
    }

    // opção 2
    //val butonClickListener = View.OnClickListener { Log.i("Teste", "Botão clicado") }

    //val butonClickListener = object : View.OnClickListener {
    //    override fun onClick(v: View?) {
    //        Log.i("Teste", "Botão clicado")
    //    }
    //}


    // opção 1
    fun buttonClicked(view: View) {
        // Adicionar essa tag no elemento XML
        // android:onClick="buttonClicked"
        Log.i("Teste", "Botão clicado")
    }

    private fun numberGenerator(text: String, txtResult: TextView) {
        if(text.isEmpty() || text.toInt() !in 6 .. 15) {
            Toast.makeText(this, "Informe um número entre 6 e 15", Toast.LENGTH_LONG).show()
            return
        }

        val qtd = text.toInt()

        val random = Random()
        val numbers = mutableSetOf<Int>()

        while(numbers.size != qtd) {
            val number = random.nextInt(60) // 0...59

            if (number !in numbers)
                numbers.add(number + 1)
        }

        txtResult.text = numbers.joinToString(" - ")


        // para substituir o uso da palavra "editor" varias vezes podemos fazer de outra forma:
            // Retorna um obj do tipo editor
            //val editor = prefs.edit()

            // CHAVE -> VALOR
            //editor.putString("result", txtResult.text.toString())

            // Para salvar podemos ussar o commit o apply

            // O commit é sincrono, ou seja, trava a interface até terminar
            // também retorna um Boolean informando se teve sucesso ou não

            // O apply é asincrono e não trava a interface
            // mas não informa se deu tudo certo

            // O Google recomanda considerar usar o apply

            //editor.apply()

        // Desta forma ele ja executa tudo dentro com o editor. antes
        prefs.edit().apply {
            putString("result", txtResult.text.toString())
            apply()
        }
    }
}