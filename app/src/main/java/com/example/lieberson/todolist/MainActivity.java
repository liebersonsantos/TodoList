package com.example.lieberson.todolist;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private Button botaoAdicionar;
    private EditText textoTarefa;
    private ListView listaTarefas;
    private SQLiteDatabase bancoDados;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;

    private ArrayList<Integer> ids;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            /*Recuperando os componentes do layout*/
            textoTarefa = findViewById(R.id.textoId);
            botaoAdicionar = findViewById(R.id.botaoAdicionarId);

            /*lista*/
            listaTarefas = findViewById(R.id.listViewId);


            /*Banco de Dados*/

            bancoDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);

            /*Criando as tabelas do banco de dados*/
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR)");

            /*Botao adicionar*/
            botaoAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*pegando os dados adicionados e inserindo no bando de dados*/
                    String textoDigitado = textoTarefa.getText().toString();
                    salvarTarefa(textoDigitado);

                }
            });

            listaTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    removerTarefa(ids.get(i));
                }
            });

            /*recuperar  tarefas*/
            recuperarTarefas();


        }catch (Exception e){
             e.printStackTrace();

        }

    }

    private void salvarTarefa(String texto){

        try {

            if (texto.equals("")){

                Toast.makeText(MainActivity.this, "Digite uma Tarefa", Toast.LENGTH_SHORT).show();
            }else {

                bancoDados.execSQL("INSERT INTO tarefas (tarefa) VALUES('"+ texto +"')");
                Toast.makeText(MainActivity.this, "Tarefa salva com Sucesso", Toast.LENGTH_SHORT).show();
                recuperarTarefas();
                textoTarefa.setText("");

            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private void recuperarTarefas(){

        try {

             /*listando e exibindo os dados inseridos*/
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);

            /*recuperando os ids das colunas*/
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            /*criando adaptador*/
            itens = new ArrayList<String>();
            ids = new ArrayList<Integer>();

            /*listando as tarefas*/
            if (cursor.getCount() > 0){

                cursor.moveToFirst();
                do {

                    Log.i("Resultado - ", "Tarefa: " + cursor.getString(indiceColunaTarefa));
                    itens.add(cursor.getString(indiceColunaTarefa));
                    ids.add(Integer.parseInt(cursor.getString(indiceColunaId)));
                    cursor.moveToNext();

                }while (cursor.moveToNext());
            }

            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    itens);
            listaTarefas.setAdapter(itensAdaptador);


        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private void removerTarefa(Integer id){
        try {

            bancoDados.execSQL("DELETE FROM tarefas WHERE id ="+id);
            recuperarTarefas();
            Toast.makeText(MainActivity.this, "Tarefa removida com Sucesso", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
