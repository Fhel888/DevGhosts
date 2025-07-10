// usuarios.js

document.addEventListener('DOMContentLoaded', () => {

    // --- FONTE DE DADOS (Mock Data) ---
    // MODIFICADO: Adicionado 'id' e 'status' para cada usuário
    let usuarios = [
        { id: 'USR-001', fullName: 'Ana Beatriz Costa', email: 'ana.costa@example.com', role: 'Administrador', status: 'Ativo' },
        { id: 'USR-002', fullName: 'Carlos Daniel Martins', email: 'carlos.martins@example.com', role: 'Gerente', status: 'Ativo' },
        { id: 'USR-003', fullName: 'Fernanda Lima da Silva', email: 'fernanda.silva@example.com', role: 'Usuário Padrão', status: 'Ativo' },
        { id: 'USR-004', fullName: 'Ricardo Mendes', email: 'ricardo.mendes@example.com', role: 'Usuário Padrão', status: 'Inativo' },
    ];

    // --- ESTADO DA APLICAÇÃO ---
    let searchTerm = '';
    let currentViewingUserId = null;

    // --- REFERÊNCIAS AOS ELEMENTOS DO DOM ---
    const DOMElements = {
        searchInput: document.getElementById('search-input'),
        userList: document.getElementById('user-list'),
        // Modal de Adicionar
        addUserBtn: document.getElementById('add-user-btn'),
        addModal: document.getElementById('add-user-modal'),
        closeAddModalBtn: document.getElementById('close-add-modal-btn'),
        cancelAddBtn: document.getElementById('cancel-add-btn'),
        addUserForm: document.getElementById('add-user-form'),
        errorMessageDiv: document.getElementById('error-message'),
        // ADICIONADO: Modal de Detalhes
        detailsModal: document.getElementById('user-details-modal'),
        closeDetailsModalBtn: document.getElementById('close-details-modal-btn'),
        disassociateUserBtn: document.getElementById('disassociate-user-btn'),
        detailUsuarioNome: document.getElementById('detail-usuario-nome'),
        detailUsuarioId: document.getElementById('detail-usuario-id'),
        detailUsuarioEmail: document.getElementById('detail-usuario-email'),
        detailUsuarioCargo: document.getElementById('detail-usuario-cargo'),
        detailUsuarioStatus: document.getElementById('detail-usuario-status'),
    };

    // --- FUNÇÕES ---

    /**
     * Renderiza a lista de usuários, aplicando o filtro de busca.
     */
    function render() {
        DOMElements.userList.innerHTML = '';
        const filteredUsers = usuarios.filter(user =>
            user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            user.email.toLowerCase().includes(searchTerm.toLowerCase())
        );

        if (filteredUsers.length > 0) {
            filteredUsers.forEach(user => {
                const statusClass = `status-${user.status.toLowerCase()}`;
                const userItem = document.createElement('li');
                // MODIFICADO: Adicionado classe e data-id para tornar clicável
                userItem.className = 'user-list-item';
                userItem.dataset.userId = user.id;
                
                userItem.innerHTML = `
                    <span class="user-name">${user.fullName}</span>
                    <span class="user-email">${user.email}</span>
                    <span class="user-role">${user.role}</span>
                    <span class="user-status ${statusClass}">${user.status}</span>
                `;
                DOMElements.userList.appendChild(userItem);
            });
        } else {
            DOMElements.userList.innerHTML = `<li class="no-results-item">Nenhum usuário encontrado.</li>`;
        }
    }
    
    /**
     * ADICIONADO: Função genérica para controlar a exibição de modais.
     */
    function toggleModal(modal, show) {
        if (show) {
            modal.classList.add('show');
        } else {
            modal.classList.remove('show');
        }
    }
    
    /**
     * Lida com o envio do formulário de cadastro de usuário.
     */
    function handleAddUser(event) {
        event.preventDefault();
        const formData = new FormData(DOMElements.addUserForm);
        const userData = Object.fromEntries(formData.entries());

        if (userData.password.length < 8) {
            showError('A senha deve ter no mínimo 8 caracteres.');
            return;
        }
        if (userData.password !== userData.confirmPassword) {
            showError('As senhas não coincidem.');
            return;
        }
        
        hideError();
        
        // MODIFICADO: Adiciona o usuário na lista e atualiza a tela
        const newUser = {
            id: 'USR-' + Date.now(),
            fullName: userData.fullName,
            email: userData.email,
            role: userData.role,
            status: 'Ativo' // Novos usuários entram como ativos
        };

        usuarios.unshift(newUser); // Adiciona no início do array
        alert(`Usuário "${newUser.fullName}" cadastrado com sucesso!`);
        
        toggleModal(DOMElements.addModal, false);
        DOMElements.addUserForm.reset();
        render(); // Re-renderiza a lista para incluir o novo usuário
    }

    // ADICIONADO: Funções para o modal de detalhes
    function showUserDetails(userId) {
        const user = usuarios.find(u => u.id === userId);
        if (!user) return;

        currentViewingUserId = userId;
        DOMElements.detailUsuarioNome.textContent = user.fullName;
        DOMElements.detailUsuarioId.textContent = user.id;
        DOMElements.detailUsuarioEmail.textContent = user.email;
        DOMElements.detailUsuarioCargo.textContent = user.role;
        DOMElements.detailUsuarioStatus.innerHTML = `<span class="user-status status-${user.status.toLowerCase()}">${user.status}</span>`;
        
        if (user.status === 'Inativo') {
            DOMElements.disassociateUserBtn.textContent = 'Reativar Usuário';
            DOMElements.disassociateUserBtn.classList.replace('btn-danger', 'btn-primary');
        } else {
            DOMElements.disassociateUserBtn.textContent = 'Desvincular Usuário';
            DOMElements.disassociateUserBtn.classList.replace('btn-primary', 'btn-danger');
        }

        toggleModal(DOMElements.detailsModal, true);
    }

    function handleDisassociateUser() {
        if (!currentViewingUserId) return;
        const userIndex = usuarios.findIndex(u => u.id === currentViewingUserId);
        if (userIndex === -1) return;

        const user = usuarios[userIndex];
        const actionText = user.status === 'Ativo' ? 'desvincular' : 'reativar';
        
        if (confirm(`Tem certeza que deseja ${actionText} o usuário "${user.fullName}"?`)) {
            user.status = user.status === 'Ativo' ? 'Inativo' : 'Ativo';
            toggleModal(DOMElements.detailsModal, false);
            render();
        }
    }

    // Funções de feedback de erro
    function showError(message) { DOMElements.errorMessageDiv.textContent = message; DOMElements.errorMessageDiv.style.display = 'block'; }
    function hideError() { DOMElements.errorMessageDiv.style.display = 'none'; }

    /**
     * Configura todos os event listeners da aplicação.
     */
    function bindEventListeners() {
        DOMElements.searchInput.addEventListener('input', e => { searchTerm = e.target.value; render(); });
        
        // Modal de Adicionar
        DOMElements.addUserBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, true));
        DOMElements.closeAddModalBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, false));
        DOMElements.cancelAddBtn.addEventListener('click', () => toggleModal(DOMElements.addModal, false));
        DOMElements.addUserForm.addEventListener('submit', handleAddUser);
        
        // ADICIONADO: Listeners para o Modal de Detalhes
        DOMElements.userList.addEventListener('click', e => {
            const userItem = e.target.closest('.user-list-item');
            if (userItem && userItem.dataset.userId) {
                showUserDetails(userItem.dataset.userId);
            }
        });
        DOMElements.closeDetailsModalBtn.addEventListener('click', () => toggleModal(DOMElements.detailsModal, false));
        DOMElements.disassociateUserBtn.addEventListener('click', handleDisassociateUser);

        // Fechar modais ao clicar no overlay
        [DOMElements.addModal, DOMElements.detailsModal].forEach(modal => {
            modal.addEventListener('click', e => { if (e.target === modal) toggleModal(modal, false); });
        });
    }

    // Inicia a aplicação
    bindEventListeners();
    render();
});