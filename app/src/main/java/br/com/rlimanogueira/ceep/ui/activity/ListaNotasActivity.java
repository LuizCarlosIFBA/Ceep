package br.com.rlimanogueira.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.rlimanogueira.ceep.R;
import br.com.rlimanogueira.ceep.dao.NotaDAO;
import br.com.rlimanogueira.ceep.model.Nota;
import br.com.rlimanogueira.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.rlimanogueira.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.rlimanogueira.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.rlimanogueira.ceep.ui.activity.notaActivityConstantes.CHAVE_NOTA;
import static br.com.rlimanogueira.ceep.ui.activity.notaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.rlimanogueira.ceep.ui.activity.notaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.rlimanogueira.ceep.ui.activity.notaActivityConstantes.POSICAO;
import static br.com.rlimanogueira.ceep.ui.activity.notaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {


    public static final String TITULO_APPBAR = "Notas";
    private RecyclerView lista;
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);
        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent vaiParaFormulario = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(vaiParaFormulario, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if(ehResultadoInsereNota(requestCode, data)){
            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }
        }if(ehResultadoAlteraNota(requestCode, data)){
            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = verificaPosicaoValida(data);

                if(ehPosicaoValida(posicaoRecebida)){
                    altera(notaRecebida, posicaoRecebida);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int verificaPosicaoValida(Intent data) {
        return data.getIntExtra(POSICAO, POSICAO_INVALIDA);
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida>POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) && ehNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean ehNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoInsereNota(int requestCode,  Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) && ehNota(data);
    }

      private boolean resultadoOk(int resultCode) {
        return resultCode== Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode==CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        lista = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, lista);
        configuraItemTouchHelper();
    }

    private void configuraItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(lista);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView lista) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        lista.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNotaActivityAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

}
