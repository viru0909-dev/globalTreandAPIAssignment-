// Global Trend API Integration - Frontend JavaScript
// Handles all API calls and UI interactions

const API_BASE = '';  // Using same origin

// ===== Navigation =====
document.addEventListener('DOMContentLoaded', () => {
    // Setup navigation
    const navButtons = document.querySelectorAll('.nav-btn');
    navButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const section = btn.getAttribute('data-section');
            switchSection(section);
        });
    });
});

function switchSection(sectionName) {
    // Update navigation buttons
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.getAttribute('data-section') === sectionName) {
            btn.classList.add('active');
        }
    });

    // Update sections
    document.querySelectorAll('.api-section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(`${sectionName}-section`).classList.add('active');
}

// ===== Loading Overlay =====
function showLoading() {
    document.getElementById('loading-overlay').classList.remove('hidden');
}

function hideLoading() {
    document.getElementById('loading-overlay').classList.add('hidden');
}

// ===== Error Handling =====
function showError(container, message) {
    container.innerHTML = `
        <div class="error-card">
            <div class="error-icon">⚠️</div>
            <h3>Error</h3>
            <p>${escapeHtml(message)}</p>
        </div>
    `;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ===== GitHub API Functions =====

async function fetchGitHubUsers() {
    const since = document.getElementById('github-since').value || 0;
    const perPage = document.getElementById('github-per-page').value || 10;
    const container = document.getElementById('github-content');

    showLoading();

    try {
        const response = await fetch(`${API_BASE}/api/github/users?since=${since}&perPage=${perPage}`);
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to fetch GitHub users');
        }

        const result = await response.json();
        
        if (!result.success || !result.data) {
            throw new Error('Invalid response from server');
        }

        displayGitHubUsers(result.data, container);

    } catch (error) {
        console.error('Error fetching GitHub users:', error);
        showError(container, error.message);
    } finally {
        hideLoading();
    }
}

function displayGitHubUsers(users, container) {
    if (!users || users.length === 0) {
        container.innerHTML = `
            <div class="welcome-card">
                <div class="welcome-icon">🔍</div>
                <h3>No Users Found</h3>
                <p>Try a different starting ID or increase the per-page limit</p>
            </div>
        `;
        return;
    }

    const grid = document.createElement('div');
    grid.className = 'grid';

    users.forEach(user => {
        const card = document.createElement('div');
        card.className = 'card';
        card.onclick = () => fetchGitHubUserDetails(user.login);

        card.innerHTML = `
            <div class="card-header">
                <img src="${user.avatarUrl}" alt="${escapeHtml(user.login)}" class="avatar">
                <div>
                    <div class="card-title">${escapeHtml(user.login)}</div>
                    <div class="card-subtitle">ID: ${user.id}</div>
                </div>
            </div>
            <div class="card-footer">
                <div class="stat">
                    <span class="stat-icon">🔗</span>
                    <span>Profile</span>
                </div>
            </div>
        `;

        grid.appendChild(card);
    });

    container.innerHTML = '';
    container.appendChild(grid);
}

async function fetchGitHubUserDetails(username) {
    const container = document.getElementById('github-content');
    showLoading();

    try {
        const response = await fetch(`${API_BASE}/api/github/users/${username}`);
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to fetch user details');
        }

        const result = await response.json();
        
        if (!result.success || !result.data) {
            throw new Error('Invalid response from server');
        }

        displayGitHubUserDetails(result.data, container);
        
        // Fetch repositories
        fetchUserRepositories(username);

    } catch (error) {
        console.error('Error fetching user details:', error);
        showError(container, error.message);
    } finally {
        hideLoading();
    }
}

function displayGitHubUserDetails(user, container) {
    container.innerHTML = `
        <div class="detail-view">
            <button class="back-button" onclick="fetchGitHubUsers()">
                ← Back to Users
            </button>
            
            <div class="detail-header">
                <img src="${user.avatarUrl}" alt="${escapeHtml(user.login)}" class="detail-avatar">
                <h2 class="detail-title">${user.name || escapeHtml(user.login)}</h2>
                <div class="detail-subtitle">@${escapeHtml(user.login)}</div>
            </div>

            <div class="detail-body">
                ${user.bio ? `<p class="card-text">${escapeHtml(user.bio)}</p>` : ''}
                
                <div class="detail-info">
                    ${user.company ? `
                        <div class="info-item">
                            <div class="info-label">Company</div>
                            <div class="info-value">${escapeHtml(user.company)}</div>
                        </div>
                    ` : ''}
                    ${user.location ? `
                        <div class="info-item">
                            <div class="info-label">Location</div>
                            <div class="info-value">${escapeHtml(user.location)}</div>
                        </div>
                    ` : ''}
                    ${user.email ? `
                        <div class="info-item">
                            <div class="info-label">Email</div>
                            <div class="info-value">${escapeHtml(user.email)}</div>
                        </div>
                    ` : ''}
                    ${user.blog ? `
                        <div class="info-item">
                            <div class="info-label">Website</div>
                            <div class="info-value"><a href="${user.blog}" target="_blank" style="color: var(--primary);">${escapeHtml(user.blog)}</a></div>
                        </div>
                    ` : ''}
                </div>

                <div class="detail-info" style="margin-top: 1.5rem;">
                    <div class="info-item">
                        <div class="info-label">Public Repos</div>
                        <div class="info-value">${user.publicRepos || 0}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Followers</div>
                        <div class="info-value">${user.followers || 0}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Following</div>
                        <div class="info-value">${user.following || 0}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Public Gists</div>
                        <div class="info-value">${user.publicGists || 0}</div>
                    </div>
                </div>
            </div>

            <div class="detail-section" id="repositories-section">
                <h3>📚 Repositories</h3>
                <div id="repositories-content">
                    <div style="text-align: center; padding: 2rem; color: var(--text-secondary);">
                        Loading repositories...
                    </div>
                </div>
            </div>
        </div>
    `;
}

async function fetchUserRepositories(username) {
    const container = document.getElementById('repositories-content');

    try {
        const response = await fetch(`${API_BASE}/api/github/users/${username}/repos?page=1&perPage=10`);
        
        if (!response.ok) {
            throw new Error('Failed to fetch repositories');
        }

        const result = await response.json();
        
        if (!result.success || !result.data) {
            throw new Error('Invalid response from server');
        }

        displayRepositories(result.data, container);

    } catch (error) {
        console.error('Error fetching repositories:', error);
        container.innerHTML = `
            <div style="text-align: center; padding: 2rem; color: var(--error);">
                Failed to load repositories
            </div>
        `;
    }
}

function displayRepositories(repos, container) {
    if (!repos || repos.length === 0) {
        container.innerHTML = `
            <div style="text-align: center; padding: 2rem; color: var(--text-secondary);">
                No public repositories found
            </div>
        `;
        return;
    }

    const grid = document.createElement('div');
    grid.className = 'grid';

    repos.forEach(repo => {
        const card = document.createElement('div');
        card.className = 'card';
        card.onclick = () => window.open(repo.htmlUrl, '_blank');

        card.innerHTML = `
            <div class="card-title">${escapeHtml(repo.name)}</div>
            ${repo.description ? `<p class="card-text">${escapeHtml(repo.description)}</p>` : '<p class="card-text" style="color: var(--text-secondary); font-style: italic;">No description</p>'}
            <div class="card-footer">
                ${repo.language ? `
                    <div class="stat">
                        <span class="stat-icon">💻</span>
                        <span>${escapeHtml(repo.language)}</span>
                    </div>
                ` : ''}
                <div class="stat">
                    <span class="stat-icon">⭐</span>
                    <span>${repo.stargazersCount || 0}</span>
                </div>
                <div class="stat">
                    <span class="stat-icon">🔱</span>
                    <span>${repo.forksCount || 0}</span>
                </div>
            </div>
        `;

        grid.appendChild(card);
    });

    container.innerHTML = '';
    container.appendChild(grid);
}

// ===== Posts API Functions =====

async function fetchPosts() {
    const userId = document.getElementById('post-user-id').value;
    const limit = document.getElementById('post-limit').value;
    const container = document.getElementById('posts-content');

    showLoading();

    try {
        let url = `${API_BASE}/api/posts?`;
        if (userId) url += `userId=${userId}&`;
        if (limit) url += `limit=${limit}`;

        const response = await fetch(url);
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to fetch posts');
        }

        const result = await response.json();
        
        if (!result.success || !result.data) {
            throw new Error('Invalid response from server');
        }

        displayPosts(result.data, container);

    } catch (error) {
        console.error('Error fetching posts:', error);
        showError(container, error.message);
    } finally {
        hideLoading();
    }
}

function displayPosts(posts, container) {
    if (!posts || posts.length === 0) {
        container.innerHTML = `
            <div class="welcome-card">
                <div class="welcome-icon">📭</div>
                <h3>No Posts Found</h3>
                <p>Try different filter options</p>
            </div>
        `;
        return;
    }

    const grid = document.createElement('div');
    grid.className = 'grid';

    posts.forEach(post => {
        const card = document.createElement('div');
        card.className = 'card';
        card.onclick = () => fetchPostDetails(post.id);

        card.innerHTML = `
            <div class="card-header">
                <div>
                    <div class="card-title">${escapeHtml(post.title)}</div>
                    <div class="card-subtitle">Post #${post.id} • User #${post.userId}</div>
                </div>
            </div>
            <p class="card-text">${escapeHtml(post.body.substring(0, 100))}${post.body.length > 100 ? '...' : ''}</p>
            <div class="card-footer">
                <div class="stat">
                    <span class="stat-icon">📖</span>
                    <span>Read More</span>
                </div>
            </div>
        `;

        grid.appendChild(card);
    });

    container.innerHTML = '';
    container.appendChild(grid);
}

async function fetchPostDetails(postId) {
    const container = document.getElementById('posts-content');
    showLoading();

    try {
        const response = await fetch(`${API_BASE}/api/posts/${postId}`);
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to fetch post details');
        }

        const result = await response.json();
        
        if (!result.success || !result.data) {
            throw new Error('Invalid response from server');
        }

        displayPostDetails(result.data, container);

    } catch (error) {
        console.error('Error fetching post details:', error);
        showError(container, error.message);
    } finally {
        hideLoading();
    }
}

function displayPostDetails(post, container) {
    container.innerHTML = `
        <div class="detail-view">
            <button class="back-button" onclick="fetchPosts()">
                ← Back to Posts
            </button>
            
            <div class="detail-header">
                <div class="detail-title">${escapeHtml(post.title)}</div>
                <div class="detail-subtitle">Post #${post.id} • By User #${post.userId}</div>
            </div>

            <div class="detail-body">
                <div class="detail-section">
                    <h3>Content</h3>
                    <p class="card-text" style="font-size: 1.125rem; line-height: 1.8;">
                        ${escapeHtml(post.body)}
                    </p>
                </div>

                <div class="detail-info">
                    <div class="info-item">
                        <div class="info-label">Post ID</div>
                        <div class="info-value">${post.id}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">User ID</div>
                        <div class="info-value">
                            <a href="#" onclick="fetchUserDetails(${post.userId}); return false;" style="color: var(--primary);">
                                ${post.userId}
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

async function fetchUsers() {
    const container = document.getElementById('posts-content');
    showLoading();

    try {
        const response = await fetch(`${API_BASE}/api/users`);
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to fetch users');
        }

        const result = await response.json();
        
        if (!result.success || !result.data) {
            throw new Error('Invalid response from server');
        }

        displayUsers(result.data, container);

    } catch (error) {
        console.error('Error fetching users:', error);
        showError(container, error.message);
    } finally {
        hideLoading();
    }
}

function displayUsers(users, container) {
    if (!users || users.length === 0) {
        container.innerHTML = `
            <div class="welcome-card">
                <div class="welcome-icon">👥</div>
                <h3>No Users Found</h3>
            </div>
        `;
        return;
    }

    const grid = document.createElement('div');
    grid.className = 'grid';

    users.forEach(user => {
        const card = document.createElement('div');
        card.className = 'card';
        card.onclick = () => fetchUserDetails(user.id);

        card.innerHTML = `
            <div class="card-title">${escapeHtml(user.name)}</div>
            <div class="card-subtitle" style="color: var(--primary); margin-bottom: 0.5rem;">@${escapeHtml(user.username)}</div>
            <p class="card-text">
                📧 ${escapeHtml(user.email)}<br>
                📞 ${escapeHtml(user.phone)}<br>
                🌐 ${escapeHtml(user.website)}
            </p>
            <div class="card-footer">
                <div class="stat">
                    <span class="stat-icon">🏢</span>
                    <span>${escapeHtml(user.company.name)}</span>
                </div>
            </div>
        `;

        grid.appendChild(card);
    });

    container.innerHTML = '';
    container.appendChild(grid);
}

async function fetchUserDetails(userId) {
    const container = document.getElementById('posts-content');
    showLoading();

    try {
        const response = await fetch(`${API_BASE}/api/users/${userId}`);
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to fetch user details');
        }

        const result = await response.json();
        
        if (!result.success || !result.data) {
            throw new Error('Invalid response from server');
        }

        displayUserDetails(result.data, container);

    } catch (error) {
        console.error('Error fetching user details:', error);
        showError(container, error.message);
    } finally {
        hideLoading();
    }
}

function displayUserDetails(user, container) {
    container.innerHTML = `
        <div class="detail-view">
            <button class="back-button" onclick="fetchUsers()">
                ← Back to Users
            </button>
            
            <div class="detail-header">
                <h2 class="detail-title">${escapeHtml(user.name)}</h2>
                <div class="detail-subtitle">@${escapeHtml(user.username)} • ID: ${user.id}</div>
            </div>

            <div class="detail-body">
                <div class="detail-section">
                    <h3>Contact Information</h3>
                    <div class="detail-info">
                        <div class="info-item">
                            <div class="info-label">Email</div>
                            <div class="info-value">${escapeHtml(user.email)}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Phone</div>
                            <div class="info-value">${escapeHtml(user.phone)}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Website</div>
                            <div class="info-value">
                                <a href="http://${user.website}" target="_blank" style="color: var(--primary);">
                                    ${escapeHtml(user.website)}
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3>Address</h3>
                    <div class="detail-info">
                        <div class="info-item">
                            <div class="info-label">Street</div>
                            <div class="info-value">${escapeHtml(user.address.street)}, ${escapeHtml(user.address.suite)}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">City</div>
                            <div class="info-value">${escapeHtml(user.address.city)}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Zipcode</div>
                            <div class="info-value">${escapeHtml(user.address.zipcode)}</div>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3>Company</h3>
                    <div class="detail-info">
                        <div class="info-item">
                            <div class="info-label">Name</div>
                            <div class="info-value">${escapeHtml(user.company.name)}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Catch Phrase</div>
                            <div class="info-value">${escapeHtml(user.company.catchPhrase)}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Business</div>
                            <div class="info-value">${escapeHtml(user.company.bs)}</div>
                        </div>
                    </div>
                </div>

                <div style="margin-top: 1.5rem;">
                    <button class="btn-primary" onclick="fetchPosts(); document.getElementById('post-user-id').value=${user.id}; setTimeout(fetchPosts, 100);">
                        View Posts by This User
                    </button>
                </div>
            </div>
        </div>
    `;
}
