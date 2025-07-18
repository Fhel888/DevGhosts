package com.example.appblessfuture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoControllerUsuarios {
    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoControllerUsuarios(Context context){
        banco = new CriaBanco(context);
    }
    public boolean verificarEmailCadastrado(String email) {
        db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE email = ?", new String[]{email});

        boolean resposta = cursor.getCount() > 0;
        cursor.close();

        db.close();
        return resposta;
    }
    public String inserirDados(String Nome, String User, String Setor, String Cel,
                               String Email, String Senha){
        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("nome", Nome);
        valores.put("user", User);
        valores.put("setor", Setor);
        valores.put("celular", Cel);
        valores.put("email", Email);
        valores.put("senha", Senha);

        resultado = db.insert("usuarios", null, valores);
        db.close();

        if (resultado == -1)
            return "Erro ao cadastrar";
        else
            return "Cadastro efetuado com sucesso";
    }
    public Cursor consultarLogin (String Email, String Senha){
        Cursor cursor;
        String[] campos = {"codigo", "nome", "user", "setor", "celular", "email", "senha"};
        String where = "email = '" + Email + "' and senha = '" + Senha + "'";

        db = banco.getReadableDatabase();

        cursor = db.query("usuarios", campos, where, null, null,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
    public String alterarDados (int Codigo, String Nome, String User, String Setor, String Cel, String Email, String Senha) {
        String msg = "";
        db = banco.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome", Nome);
        valores.put("user", User);
        valores.put("setor", Setor);
        valores.put("celular", Cel);
        valores.put("email", Email);
        valores.put("senha", Senha);

        int linhas = db.update("usuarios", valores, "codigo = ?", new String[]{String.valueOf(Codigo)});
        db.close();

        if (linhas > 0) {
            msg = "Dados alterados com sucesso!";
        } else {
            msg = "Erro ao atualizar os dados!";
        }
        return msg;
    }
    public Cursor trazerUsuario (int Codigo){
        Cursor cursor;
        String[] campos = {"codigo", "nome", "user", "setor", "celular", "email", "senha"};
        String where = "codigo = " + Codigo;

        db = banco.getReadableDatabase();
        cursor = db.query("usuarios", campos, where, null,null,
                null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public boolean verificarEmailRecup (String Email) {
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE email = ?", new String[]{Email});

        boolean resposta = cursor.moveToFirst();
        cursor.close();

        return resposta;
    }
    public String atualizarSenhaRecup (String Email, String NovaSenha) {
        String msg = "";
        db = banco.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("senha", NovaSenha);

        int linhas = db.update("usuarios", valores, "email = ?", new String[]{Email});
        db.close();

        if (linhas > 0) {
            msg = "Senha alterada com sucesso!";
        } else {
            msg = "Erro ao atualizar a senha!";
        }
        return msg;
    }
    public String excluirDados (int codigo) {
        String msg = "";
        db = banco.getWritableDatabase();

        int linhas = db.delete("usuarios", "codigo=?", new String[]{String.valueOf(codigo)});
        db.close();

        if (linhas > 0) {
            msg = "Conta excluida com sucesso!";
        } else {
            msg = "Erro ao excluir a conta!";
        }
        return msg;
    }
}