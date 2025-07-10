/**
 * Adiciona um listener que garante que o script só rode
 * depois que todo o conteúdo da página (DOM) for carregado.
 */
document.addEventListener('DOMContentLoaded', () => {

    // --- FONTE DE DADOS (Mock Data) ---
    let clientes = [
        { nome: 'TecnoCorp', id: 'CLI-001', cnpj: '12.345.678/0001-99', email: 'contato@tecnocorp.com', telefone: '(11) 98765-4321', responsavel: 'Carlos Silva', dataCadastro: '2024-01-15', processos: [ { id: 'PROC-101', nome: 'Implantação de Sistema', status: 'Em Andamento' }, { id: 'PROC-102', nome: 'Consultoria de TI', status: 'Concluído' } ] },
        { nome: 'Soluções Alfa', id: 'CLI-002', cnpj: '23.456.789/0001-88', email: 'alfa@solucoesalfa.com.br', telefone: '(21) 91234-5678', responsavel: 'Beatriz Costa', dataCadastro: '2024-02-20', processos: [ { id: 'PROC-103', nome: 'Otimização de Processos', status: 'Em Andamento' } ] },
        { nome: 'Grupo Beta', id: 'CLI-003', cnpj: '34.567.890/0001-77', email: 'suporte@grupobeta.com', telefone: '(31) 95555-4444', responsavel: 'Daniel Martins', dataCadastro: '2025-03-10', processos: [] },
        { nome: 'Inovações Gama', id: 'CLI-004', cnpj: '45.678.901/0001-66', email: 'gama@inovacoes.dev', telefone: '(41) 98888-7777', responsavel: 'Fernanda Lima', dataCadastro: '2023-04-05', processos: [ { id: 'PROC-104', nome: 'Desenvolvimento de App', status: 'Pausado' } ] },
        // ... mais clientes
    ];

    // --- ESTADO DA APLICAÇÃO ---
    let currentPage = 1;
    const itemsPerPage = 10;
    let currentClientBeingViewedId = null;
    let searchTerm = '';
    let currentSort = 'name-asc'; // NOVO: Estado inicial da ordenação.

    // --- REFERÊNCIAS AOS ELEMENTOS DO DOM ---
    const DOMElements = {
        sortSelect: document.getElementById('sort-clients-select'), // NOVO: Seletor de ordenação.
        searchInput: document.getElementById('search-input'),
        clientList: document.getElementById('client-list'),
        prevButton: document.getElementById('prev-button'),
        nextButton: document.getElementById('next-button'),
        pageInfo: document.getElementById('page-info'),
        addClientBtn: document.getElementById('add-client-btn'),
        addModal: document.getElementById('add-client-modal'),
        closeAddModalBtn: document.getElementById('close-add-modal-btn'),
        cancelAddBtn: document.getElementById('cancel-add-btn'),
        addClientForm: document.getElementById('add-client-form'),
        detailsModal: document.getElementById('client-details-modal'),
        closeDetailsModalBtn: document.getElementById('close-details-modal-btn'),
        deleteClientBtn: document.getElementById('delete-client-btn'),
        detailNome: document.getElementById('detail-nome'),
        detailCnpj: document.getElementById('detail-cnpj'),
        detailEmail: document.getElementById('detail-email'),
        detailTelefone: document.getElementById('detail-telefone'),
        detailResponsavel: document.getElementById('detail-responsavel'),
        detailDataCadastro: document.getElementById('detail-data-cadastro'),
        detailProcessList: document.getElementById('detail-process-list'),
    };

    // --- FUNÇÕES ---

    /**
     * ATUALIZADO: Renderiza a lista de clientes, aplicando filtro E ordenação.
     */
    function render() {
        DOMElements.clientList.innerHTML = '';
        
        // 1. Filtra os clientes com base no termo de busca
        let processedClients = clientes.filter(cliente => 
            cliente.nome.toLowerCase().includes(searchTerm.toLowerCase())
        );

        // 2. NOVO: Ordena os dados já filtrados
        switch (currentSort) {
            case 'name-asc':
                processedClients.sort((a, b) => a.nome.localeCompare(b.nome));
                break;
            case 'name-desc':
                processedClients.sort((a, b) => b.nome.localeCompare(a.nome));
                break;
            case 'date-desc':
                processedClients.sort((a, b) => new Date(b.dataCadastro) - new Date(a.dataCadastro));
                break;
            case 'date-asc':
                processedClients.sort((a, b) => new Date(a.dataCadastro) - new Date(b.dataCadastro));
                break;
        }

        // 3. Pagina os dados já filtrados e ordenados
        const startIndex = (currentPage - 1) * itemsPerPage;
        const paginatedClients = processedClients.slice(startIndex, startIndex + itemsPerPage);

        if (paginatedClients.length > 0) {
            const clientsHTML = paginatedClients.map(cliente => `
                <li class="client-item" data-client-id="${cliente.id}">
                    <span class="client-name">${cliente.nome}</span>
                    <span class="client-id">${cliente.id}</span>
                </li>
            `).join('');
            DOMElements.clientList.innerHTML = clientsHTML;
        } else {
            DOMElements.clientList.innerHTML = `<li class="no-results-item">Nenhum cliente encontrado.</li>`;
        }

        updatePaginationControls(processedClients.length);
    }

    function updatePaginationControls(totalItems) {
        const totalPages = Math.ceil(totalItems / itemsPerPage) || 1;
        DOMElements.pageInfo.textContent = `Página ${currentPage} de ${totalPages}`;
        DOMElements.prevButton.disabled = currentPage === 1;
        DOMElements.nextButton.disabled = currentPage === totalPages;
    }
    
    // ... (As funções showClientDetails, handleDeleteClient, etc., permanecem as mesmas)
    // As funções abaixo continuam iguais às que você já tem
    function showClientDetails(clienteId) {
        const cliente = clientes.find(c => c.id === clienteId);
        if (!cliente) return;
        currentClientBeingViewedId = cliente.id;
        const { nome, cnpj, email, telefone, responsavel, dataCadastro, processos } = cliente;
        DOMElements.detailNome.textContent = nome;
        DOMElements.detailCnpj.textContent = cnpj;
        DOMElements.detailEmail.textContent = email;
        DOMElements.detailTelefone.textContent = telefone;
        DOMElements.detailResponsavel.textContent = responsavel;
        DOMElements.detailDataCadastro.textContent = new Date(dataCadastro + 'T03:00:00Z').toLocaleDateString('pt-BR');
        if (processos && processos.length > 0) {
            DOMElements.detailProcessList.innerHTML = processos.map(proc => `<li class="process-item" data-process-id="${proc.id}"><span class="process-name">${proc.nome} (${proc.id})</span><span class="process-status status-${proc.status.toLowerCase().replace(' ', '-')}">${proc.status}</span></li>`).join('');
        } else {
            DOMElements.detailProcessList.innerHTML = `<li class="no-process-item">Nenhum processo cadastrado.</li>`;
        }
        toggleModal(DOMElements.detailsModal, true);
    }
    function handleDeleteClient() {
        if (!currentClientBeingViewedId) return;
        const clienteParaExcluir = clientes.find(c => c.id === currentClientBeingViewedId);
        if (!clienteParaExcluir) return;
        const isConfirmed = confirm(`Você tem certeza que deseja excluir o cliente "${clienteParaExcluir.nome}"? Esta ação não pode ser desfeita.`);
        if (isConfirmed) {
            console.log(`ENVIANDO REQUISIÇÃO DELETE PARA O BACKEND: /api/clientes/${currentClientBeingViewedId}`);
            clientes = clientes.filter(c => c.id !== currentClientBeingViewedId);
            alert(`Cliente "${clienteParaExcluir.nome}" excluído com sucesso!`);
            toggleModal(DOMElements.detailsModal, false);
            render();
        }
    }
    function handleAddClient(event) {
        event.preventDefault();
        const formData = new FormData(DOMElements.addClientForm);
        const newClientData = Object.fromEntries(formData.entries());
        console.log('DADOS A SEREM ENVIADOS PARA O BACKEND:', newClientData);
        alert(`Front-end: Preparando para enviar "${newClientData['company-name']}" para o back-end!`);
        toggleModal(DOMElements.addModal, false);
    }
    function toggleModal(modal, show) {
        modal.classList.toggle('show', show);
        if (!show) {
            if (modal === DOMElements.addModal) DOMElements.addClientForm.reset();
            else if (modal === DOMElements.detailsModal) currentClientBeingViewedId = null;
        }
    }

    /**
     * Configura todos os event listeners da aplicação.
     */
    function bindEventListeners() {
        DOMElements.searchInput.addEventListener('input', (event) => {
            searchTerm = event.target.value;
            currentPage = 1;
            render();
        });

        // NOVO: Listener para o seletor de ordenação
        DOMElements.sortSelect.addEventListener('change', (event) => {
            currentSort = event.target.value;
            currentPage = 1; // Volta para a primeira página ao reordenar
            render();
        });

        DOMElements.prevButton.addEventListener('click', () => { currentPage--; render(); });
        DOMElements.nextButton.addEventListener('click', () => { currentPage++; render(); });

        DOMElements.clientList.addEventListener('click', (event) => {
            const clientItem = event.target.closest('.client-item');
            if (clientItem) showClientDetails(clientItem.dataset.clientId);
        });

        DOMElements.detailProcessList.addEventListener('click', (event) => {
            const processItem = event.target.closest('.process-item');
            if (processItem) window.location.href = `processo-detalhe.html?id=${processItem.dataset.processId}`;
        });

        DOMElements.addClientForm.addEventListener('submit', handleAddClient);
        DOMElements.deleteClientBtn.addEventListener('click', handleDeleteClient);
        DOMElements.addClientBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, true));
        DOMElements.closeAddModalBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, false));
        DOMElements.cancelAddBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, false));
        DOMElements.closeDetailsModalBtn.addEventListener('click', () => toggleModal(DOMElements.detailsModal, false));
        DOMElements.addModal.addEventListener('click', (event) => { if (event.target === DOMElements.addModal) toggleModal(DOMElements.addModal, false); });
        DOMElements.detailsModal.addEventListener('click', (event) => { if (event.target === DOMElements.detailsModal) toggleModal(DOMElements.detailsModal, false); });
    }

    /**
     * Função de inicialização da aplicação.
     */
    function init() {
        bindEventListeners();
        render();
    }

    // Inicia a aplicação
    init();
});