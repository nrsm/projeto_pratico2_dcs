package pt.ipleiria.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("appMusic", 0);
        Set<String> musicSet = sp.getStringSet("musicKey", new HashSet<String>());

        music = new ArrayList<String>(musicSet);

//        music.add("Soja - Amid the Noise and Haste \n 2014 -- 5 Estrelas");
//        music.add("Da Weasel - Amor, Escárnio e Maldizer\n 2007 -- 4 Estrelas");
//        music.add("Bob Marley - Natty Dread\n 1974 -- 4 Estrelas");
//        music.add("JAMES - Gold Mother\n 1990 -- 3 Estrelas");
//        music.add("Michael Bublé - Crazy Love\n 2009 -- 4 Estrelas");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, music);

        ListView listView = (ListView) findViewById(R.id.listView_albuns);
        listView.setAdapter(adapter);

        Spinner s = (Spinner) findViewById(R.id.spinner_search);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        s.setAdapter(adapterS);


        //UM LONGO CLIQUE NA LISTVIEW

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //codigo que é executado quando se clica num item da listview
                Toast.makeText(MainActivity.this, "Eliminou o item " + position, Toast.LENGTH_SHORT).show();

                music.remove(position);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, music);

                ListView listView = (ListView) findViewById(R.id.listView_albuns);
                listView.setAdapter(adapter);

                return false;
            }
        });


        //APENAS PARA UM CLIQUE NA LISTVIEW

//    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //codigo que é executado quando se clica num item da listview
//            Toast.makeText(MainActivity.this, "Eliminou o item " + position, Toast.LENGTH_SHORT).show();
//
//            music.remove(position);
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
//                    android.R.layout.simple_list_item_1, music);
//
//            ListView listView = (ListView) findViewById(R.id.listView_albuns);
//            listView.setAdapter(adapter);
//        }
//    });

}

    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(MainActivity.this, "A Guardar Dados.", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("appMusic", 0);

        SharedPreferences.Editor edit = sp.edit();

        HashSet musicSet = new HashSet(music);


        edit.putStringSet("musicKey", musicSet);

        edit.commit();



    }

    public void onClick_search(View view) {
        //buscar referencias para a editText, o spinner e a listView
        EditText et = (EditText) findViewById(R.id.editText_search);
        Spinner sp = (Spinner) findViewById(R.id.spinner_search);
        ListView lv = (ListView) findViewById(R.id.listView_albuns);


        //criar uma nova lista, que guarde os albuns pesquisados
        ArrayList<String> searchedAlbuns = new ArrayList<>();

        //percorrer todos os albuns, e adicionar os que
        // contem o termo a pesquisar à nova lista
        String termo = et.getText().toString();
        String selectedItem = (String) sp.getSelectedItem();


        if (termo.equals("")) { //a edittext esta vazia?
            //mostra os albuns todos
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, music);

            lv.setAdapter(adapter);
            Toast.makeText(MainActivity.this, "A mostrar os albuns todos", Toast.LENGTH_SHORT).show();
        } else { //a edittext nao esta vazia

            if (selectedItem.equals("Todos")) {
                for (String c : music) { //para cada c em albuns
                    if (c.contains(termo)) { //se o c contiver o termos
                        searchedAlbuns.add(c); //adicionar o c à lista searchedAlbuns
                    }
                }
            } else if (selectedItem.equals(getString(R.string.artista))) {
                //pesquisa pelo artista
                for (String c : music) {
                    String[] split = c.split("\\-");
                    String name = split[0];

                    if (name.contains(termo)) {
                        searchedAlbuns.add(c);
                    }
                }
            } else if (selectedItem.equals("Albúm")) {
                //pesquisa pelo album
                for (String c : music) {
                    String[] split = c.split("\\-");
                    String album = split[1];
                    album = album.trim(); //trim() para se livrar de quaisquer espaços em branco no início e no final de uma string

                    if (album.contains(termo)) {
                        searchedAlbuns.add(c);
                    }
                }
            }else if (selectedItem.equals("Ano de Lançamento")) {
                //pesquisa pelo ano lancamento
                for (String c : music) {
                    String[] split = c.split("\\n");
                    String year = split[1];
                    year = year.trim();

                    if (year.contains(termo)) {
                        searchedAlbuns.add(c);
                    }
                }
            }else if (selectedItem.equals("Avaliação")) {
                //pesquisa pela avaliacao
                for (String c : music) {
                    String[] split = c.split("\\--");
                    String avaliar = split[1];
                    avaliar = avaliar.trim();

                    if (avaliar.contains(termo)) {
                        searchedAlbuns.add(c);
                    }
                }
            }
            boolean vazia = searchedAlbuns.isEmpty();

            if (vazia == false) { //se a lista de resultados nao estiver vazia
                //mostrar o conteudo da lista de albuns pesquisados na listview
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, searchedAlbuns);

                lv.setAdapter(adapter);

                //mostrar uma mensagem a dizer "Showing searched contacts."
                Toast.makeText(MainActivity.this, "Pesquisa com Sucesso!", Toast.LENGTH_SHORT).show();
            } else { //lista de resultados esta vazia
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, music);

                lv.setAdapter(adapter);
                Toast.makeText(MainActivity.this, "Não há resultados", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onClick_add(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add, null));


        // Add the buttons
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                //Obter referências para as EditTexts

                //faz o cast de um diálogo "genérico" para um alertdialog
                AlertDialog al = (AlertDialog) dialog;

                EditText etArtista = (EditText) al.findViewById(R.id.editText_artist);
                EditText etAlbum = (EditText) al.findViewById(R.id.editText_album);
                EditText etLancamento = (EditText) al.findViewById(R.id.editText_year);
                RatingBar stars = (RatingBar) al.findViewById(R.id.ratingBar);

                String artista = etArtista.getText().toString();
                String album = etAlbum.getText().toString();
                String lancamento = etLancamento.getText().toString();
                int avaliacao = (int)stars.getRating() ;

                //criar um novo album

                String newAlbum = artista + " - " + album + "\n" + lancamento + " -- " + avaliacao  + " Estrelas";

                //adicionar o album à lista de albuns

                music.add(newAlbum);

                //dizer à listview para se actualizar

                ListView lv = (ListView) findViewById(R.id.listView_albuns);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, music);

                lv.setAdapter(adapter);
                Toast.makeText(MainActivity.this, "Novo Albúm Adicionado", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(MainActivity.this, "Cancelou um novo albúm! ", Toast.LENGTH_SHORT).show();

            }
        });
        // Set other dialog properties
        builder.setTitle("NOVO ALBÚM");
        builder.setMessage("Introduza a informação do Albúm:");




        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
