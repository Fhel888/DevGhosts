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

<h2>ERP - Processos</h2>
<style>
  body { font-family: sans-serif; margin: 2em; background-color: #708b8b; }
  table { width: 100%; border-collapse: collapse; box-shadow: 0 2px 3px rgba(0,0,0,0.1); }
  th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
  th { background-color: #b98357; color: white; }
  tr:nth-child(even) { background-color: #708b8b; }
  form { border: 1px solid #ddd; border-radius: 5px; padding: 1.5em; margin-bottom: 2em; background-color: #708b8b; }
  form input, form button { display: block; margin-bottom: 10px; padding: 8px; width: calc(100% - 16px); }
  form button { background-color: #b98357; color: white; border: none; cursor: pointer; }
  form button:hover { background-color: #b98357; }
  a { color: white; text-decoration: none; }
  a:hover { text-decoration: underline; }

  /* Estilos para o formulário de filtro */
  .filter-container { background-color: #708b8b; padding: 1em; border: 1px solid #ddd; border-radius: 5px; margin-bottom: 1em; display: flex; align-items: center; gap: 15px; }
  .filter-container label { font-weight: bold; }
  .filter-container select { padding: 8px; border-radius: 4px; border: 1px solid #ccc; flex-grow: 1; }
  .filter-container .btn-clear { padding: 8px 15px; background-color: #777; color: white; border-radius: 4px; text-decoration: none; }
  .filter-container .btn-clear:hover { background-color: #555; }
</style>

<h3>Adicionar Novo Processo</h3>
<form method="POST" action="upload.php" enctype="multipart/form-data" >
  <label for="nome">Nome do Processo:</label>
  <input type="text" id="nome" name="nome" required>

  <label for="setor">Setor:</label>
  <select id="setor" name="setor" required>
    <option value="">-- Selecione um Setor --</option>
    <option value="Diagnóstico Empresarial">Diagnóstico Empresarial</option>
    <option value="Estratégica e Planejamento">Estratégica e Planejamento</option>
    <option value="Processos e Operações">Processos e Operações</option>
    <option value="Financeira e Controladoria">Financeira e Controladoria</option>
    <option value="Recursos Humanos">Recursos Humanos</option>
    <option value="Marketing e Posicionamento">Marketing e Posicionamento</option>
    <option value="Suporte e Relacionamento c/ Cliente">Suporte e Relacionamento c/ Cliente</option>
  </select>
  <br><br>

  <label for="arquivo">Arquivo (PDF):</label>
  <input type="file" id="arquivo" name="arquivo" accept="application/pdf" required>
  
  <button type="submit">Enviar Processo</button>
</form>

<h3>Processos Cadastrados</h3>

<!-- Formulário de Filtro por Setor -->
<div class="filter-container">
  <label for="filtro_setor">Filtrar por Setor:</label>
  <form method="GET" action="index.php" style="border: none; padding: 0; margin: 0; flex-grow: 1;">
    <select name="setor" id="filtro_setor" onchange="this.form.submit()">
      <option value="">-- Todos os Setores --</option>
      <?php foreach ($setores_unicos as $setor_item): ?>
        <option value="<?= htmlspecialchars($setor_item['setor']) ?>" <?= ($filtro_setor == $setor_item['setor']) ? 'selected' : '' ?>>
          <?= htmlspecialchars($setor_item['setor']) ?>
        </option>
      <?php endforeach; ?>
    </select>
  </form>
  <a href="index.php" class="btn-clear">Limpar Filtro</a>
</div>

<table>
  <thead>
    <tr>
      <th>Nome</th>
      <th>Setor</th>
      <th>Data de Upload</th>
      <th>Ações</th>
    </tr>
  </thead>
  <tbody>
    <?php if (count($processos) > 0): ?>
      <?php foreach ($processos as $proc): ?>
      <tr>
        <td><?= htmlspecialchars($proc['nome']) ?></td>
        <td><?= htmlspecialchars($proc['setor']) ?></td>
        <td><?= htmlspecialchars(date('d/m/Y H:i:s', strtotime($proc['data_upload']))) ?></td>
        <td>
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
