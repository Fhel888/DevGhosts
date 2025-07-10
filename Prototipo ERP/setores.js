document.addEventListener('DOMContentLoaded', () => {
    // --- FONTE DE DADOS ---
    let setores = [
        { id: 'SET-001', nome: 'Tecnologia da Informação', responsavel: 'Ana Oliveira', usuarios: [{ id: 'USR-002', nome: 'Bruno Santos' }, { id: 'USR-005', nome: 'Eduarda Costa' }] },
        { id: 'SET-002', nome: 'Recursos Humanos', responsavel: 'Carlos Daniel Martins', usuarios: [{ id: 'USR-001', nome: 'Ana Oliveira' }] },
        { id: 'SET-003', nome: 'Financeiro', responsavel: 'Fernanda Lima', usuarios: [] },
        { id: 'SET-004', nome: 'Marketing', responsavel: 'Ricardo Mendes', usuarios: [{ id: 'USR-004', nome: 'Daniel Martins' }] },
    ];

    // --- ESTADO DA APLICAÇÃO ---
    let currentSectorBeingViewedId = null;

    // --- REFERÊNCIAS AOS ELEMENTOS DO DOM ---
    const DOMElements = {
        sectorList: document.getElementById('sector-list'),
        addSectorBtn: document.getElementById('add-sector-btn'),
        addModal: document.getElementById('add-sector-modal'),
        closeAddModalBtn: document.getElementById('close-add-modal-btn'),
        cancelAddBtn: document.getElementById('cancel-add-btn'),
        addSectorForm: document.getElementById('add-sector-form'),
        detailsModal: document.getElementById('sector-details-modal'),
        closeDetailsModalBtn: document.getElementById('close-details-modal-btn'),
        deleteSectorBtn: document.getElementById('delete-sector-btn'),
        detailSectorName: document.getElementById('detail-sector-name'),
        detailUserList: document.getElementById('detail-user-list'),
    };

    // --- FUNÇÕES ---

    function render() {
        DOMElements.sectorList.innerHTML = '';
        setores.forEach(setor => {
            const sectorItem = document.createElement('li');
            sectorItem.className = 'sector-list-item';
            sectorItem.dataset.sectorId = setor.id;
            sectorItem.innerHTML = `
                <span class="sector-name">${setor.nome}</span>
                <span class="sector-responsible">${setor.responsavel}</span>
                <span class="sector-user-count">${setor.usuarios.length} funcionário(s)</span>
            `;
            DOMElements.sectorList.appendChild(sectorItem);
        });
    }

    function toggleModal(modal, show) {
        modal.classList.toggle('show', show);
    }

    function handleAddSector(event) {
        event.preventDefault();
        const formData = new FormData(DOMElements.addSectorForm);
        const nome = formData.get('nome');
        const responsavel = formData.get('responsavel');
        if (!nome || !responsavel) {
            alert('Por favor, preencha o nome e o responsável do setor.');
            return;
        }
        const newSector = {
            id: 'SET-' + Date.now(),
            nome: nome,
            responsavel: responsavel,
            descricao: formData.get('descricao'),
            dataCriacao: new Date().toISOString().split('T')[0],
            usuarios: []
        };
        setores.unshift(newSector);
        alert(`Setor "${newSector.nome}" cadastrado com sucesso!`);
        toggleModal(DOMElements.addModal, false);
        DOMElements.addSectorForm.reset();
        render();
    }

    function showSectorDetails(sectorId) {
        const setor = setores.find(s => s.id === sectorId);
        if (!setor) return;
        currentSectorBeingViewedId = setor.id;
        DOMElements.detailSectorName.textContent = setor.nome;
        if (setor.usuarios && setor.usuarios.length > 0) {
            DOMElements.detailUserList.innerHTML = setor.usuarios.map(user => 
                `<li class="list-sub-item" data-user-id="${user.id}"><span>${user.nome}</span></li>`
            ).join('');
        } else {
            DOMElements.detailUserList.innerHTML = `<li class="no-results-item">Nenhum funcionário neste setor.</li>`;
        }
        toggleModal(DOMElements.detailsModal, true);
    }

    function handleDeleteSector() {
        if (!currentSectorBeingViewedId) return;
        const sectorToDelete = setores.find(s => s.id === currentSectorBeingViewedId);
        if (confirm(`Tem certeza que deseja excluir o setor "${sectorToDelete.nome}"?`)) {
            setores = setores.filter(s => s.id !== currentSectorBeingViewedId);
            toggleModal(DOMElements.detailsModal, false);
            render();
        }
    }

    function bindEventListeners() {
        DOMElements.sectorList.addEventListener('click', e => {
            const sectorItem = e.target.closest('.sector-list-item');
            if (sectorItem) { showSectorDetails(sectorItem.dataset.sectorId); }
        });
        
        DOMElements.addSectorBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, true));
        DOMElements.closeAddModalBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, false));
        DOMElements.cancelAddBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, false));
        DOMElements.addModal.addEventListener('click', e => {
            if(e.target === DOMElements.addModal) toggleModal(DOMElements.addModal, false);
        });
        DOMElements.addSectorForm.addEventListener('submit', handleAddSector);
        
        DOMElements.closeDetailsModalBtn.addEventListener('click', () => toggleModal(DOMElements.detailsModal, false));
        DOMElements.detailsModal.addEventListener('click', e => { 
            if (e.target === DOMElements.detailsModal) { toggleModal(DOMElements.detailsModal, false); }
        });
        DOMElements.deleteSectorBtn.addEventListener('click', handleDeleteSector);
    }

    function init() {
        bindEventListeners();
        render();
    }

    init();
});