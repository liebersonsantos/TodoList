package com.example.lieberson.todolist;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

    private Button botaoAdicionar;
    private EditText textoTarefa;
    private ListView listaTarefas;
    private SQLiteDatabase bancoDados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            /*Recuperando os componentes do layout*/
            textoTarefa = findViewById(R.id.textoId);
            botaoAdicionar = findViewById(R.id.botaoAdicionarId);
            listaTarefas = findViewById(R.id.listViewId);

            /*Banco de Dados*/

            bancoDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);

            /*Criando as tabelas do banco de dados*/
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR(30))");

            /*Botao adicionar*/
            botaoAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*pegando os dados adicionados e inserindo no bando de dados*/
                    String textoDigitado = textoTarefa.getText().toString();
                    bancoDados.execSQL("INSERT INTO tarefas(tarefa) VALUES('"+ textoDigitado +"')");

                }
            });

            /*listando e exibindo os dados inseridos*/
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas", null);

            /*recuperando os ids das colunas*/
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            /*listando as tarefas*/
            cursor.moveToFirst();

            while (cursor != null){


                Log.i("Resultado - ", "Tarefa: " + cursor.getString(indiceColunaTarefa));
                cursor.moveToNext();
            }


        }catch (Exception e){
            e.printStackTrace();

        }










    }
}
