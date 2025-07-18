<?php
include 'conexao.php';

// 1. Busca todos os setores únicos para popular o filtro
$setores_unicos = $pdo->query("SELECT DISTINCT setor FROM processos WHERE setor IS NOT NULL AND setor != '' ORDER BY setor ASC")->fetchAll();

// 2. Verifica se um filtro de setor foi aplicado via GET
$filtro_setor = $_GET['setor'] ?? null;

if ($filtro_setor) {
    // Se houver filtro, prepara a consulta para buscar processos daquele setor
    $stmt = $pdo->prepare("SELECT * FROM processos WHERE setor = ? ORDER BY data_upload DESC");
    $stmt->execute([$filtro_setor]);
    $processos = $stmt->fetchAll();
} else {
    // Se não houver filtro, busca todos os processos
    $processos = $pdo->query("SELECT * FROM processos ORDER BY data_upload DESC")->fetchAll();
}
?>

<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Minha Página com PHP</title>
    <link rel="stylesheet" href="style.css">
    <style>
      table{ margin-left: 15px; margin-right: 15px; width: 97%; border-collapse: collapse; color: white;}
      th, td {padding: 12px; text-align: left;}
      th { background-color: #AB8763; color: white; font-family:sans-serif;  }
      .btn-clear { display: block; background-color: #AB8763; cursor: pointer; margin-right: 30px; padding: 5px; width: 10%; border-radius: 8px; text-align: center;}
      a { color: white; text-decoration: none; font-family:sans-serif;}
      </style>
<!-- CABEÇALHO DO SITE ABAIXO -->
  <div class="cabecalho">
    <img src="images/logoBlessFuture.png" alt="Logo da Empresa" class="logo">
  </div>
</head>

<!-- BODY ABAIXO -->
<body>

<!-- DIVISORIA AZUL DE ADICIONAR UM NOVO PROCESSO -->
  <form method="POST" action="upload.php" enctype="multipart/form-data" >
    <div class="divisorias">
      <br>
      <h1 class="texto">ADICIONAR NOVO PROCESSO</h1>
      <h3 class="texto">Nome do Processo:</h3>
      <input type="text" id="nome" name="nome" required class="input-nome">
      <br>
      <h3 class="texto">Setor Responsável:</h3>
      <select id="setor" name="setor" required class="select">
        <option value="">Selecione um Setor</option>
        <option value="Diagnóstico Empresarial">Diagnóstico Empresarial</option>
        <option value="Estratégica e Planejamento">Estratégica e Planejamento</option>
        <option value="Processos e Operações">Processos e Operações</option>
        <option value="Financeira e Controladoria">Financeira e Controladoria</option>
        <option value="Recursos Humanos">Recursos Humanos</option>
        <option value="Marketing e Posicionamento">Marketing e Posicionamento</option>
        <option value="Suporte e Relacionamento c/ Cliente">Suporte e Relacionamento c/ Cliente</option>
      </select>
      <br>
      <h3 class="texto">Arquivo(PDF): </h3>
      <input class="input-pdf" type="file" id="arquivo" name="arquivo" accept="application/pdf" required>
      <br>
      <br>
      <button type="submit" class="buttonzao">Enviar Processo</button>
      <br>
    </div>
</form>

<!-- DIVISORIA DE CONSULTA -->
<div class="divisorias">
  <br>
  <h1 class="texto">PROCESSOS CADASTRADOS</h1>
  <h3 class="texto">Filtrar por Setor:</h3>
  <div style="display: flex;">
  <form method="GET" action="index.php" style="border: none; padding: 0; margin: 0; flex-grow: 1;">
    <select name="setor" id="filtro_setor" onchange="this.form.submit()" class="select">
      <option value="">Todos os Setores</option>
      <?php foreach ($setores_unicos as $setor_item): ?>
        <option value="<?= htmlspecialchars($setor_item['setor']) ?>" <?= ($filtro_setor == $setor_item['setor']) ? 'selected' : '' ?>>
          <?= htmlspecialchars($setor_item['setor']) ?>
        </option>
      <?php endforeach; ?>
    </select>
  </form>
  <!-- BOTÃO DE LIMPAR FILTRO -->
  <a href="index.php" class="btn-clear">Limpar Filtro</a>
  </div>
  <br>
  <br>

      <!-- TABELA ABAIXO -->
  <table>
    <thead>
      <tr>
        <th style="border-top-left-radius: 8px;">Nome</th>
        <th>Setor</th>
        <th>Data de Upload</th>
        <th style="border-top-right-radius: 8px;">Ações</th>
      </tr>
    </thead>
    <tbody style="border: 1px solid white">
      <?php if (count($processos) > 0): ?>
        <?php foreach ($processos as $proc): ?>
        <tr>
          <td style="border: 1px solid white"><?= htmlspecialchars($proc['nome']) ?></td>
          <td style="border: 1px solid white"><?= htmlspecialchars($proc['setor']) ?></td>
          <td style="border: 1px solid white"><?= htmlspecialchars(date('d/m/Y H:i:s', strtotime($proc['data_upload']))) ?></td>
          <td style="border: 1px solid white">
            <a href="uploads/<?= htmlspecialchars($proc['arquivo']) ?>" target="_blank">Ver PDF</a> |
            <a href="delete.php?id=<?= $proc['id'] ?>" onclick="return confirm('Tem certeza que deseja excluir este processo?')">Excluir</a>
          </td>
        </tr>
        <?php endforeach; ?>
      <?php else: ?>
        <tr>
          <td colspan="4" style="text-align: center;">Nenhum processo encontrado.</td>
        </tr>
      <?php endif; ?>
    </tbody>
  </table>
  <br>
  <br> 
</div>
<br>
</body>
<footer>
  <div class="cabecalho" style="text-align: center; ">
    <img src="images/logoBlessFuture.png" alt="Logo da Empresa" class="logo" style="">
    <p style="color: white; font-family:sans-serif; text-align: center;">&copy; 2025 Simulador ERP. Designed by DevGhosts.</p>
  <br><br>
  </div>



</footer>
</html>