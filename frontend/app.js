// DOM Elements
const propertyGrid = document.getElementById('propertyGrid');
const filterBtns = document.querySelectorAll('.filter-btn');
const searchBtn = document.getElementById('searchBtn');

// Initial Render & Setup
document.addEventListener('DOMContentLoaded', async () => {
    // Add API Script First in Index
    if (typeof window.api === 'undefined') {
        alert("api.js not loaded!");
        return;
    }
    await loadInitialProperties();

    // Setup Event Listeners
    setupSearchLogic();
    setupFilterLogic();
});

async function loadInitialProperties() {
    propertyGrid.innerHTML = '<p style="text-align:center; width:100%; color:var(--text-secondary);">Loading luxury stays...</p>';
    try {
        const properties = await window.api.getRooms();
        renderProperties(properties);
    } catch (e) {
        propertyGrid.innerHTML = '<p style="color:red;">Error loading properties.</p>';
    }
}

// Render Function
async function renderProperties(properties) {
    propertyGrid.innerHTML = '';

    const favs = await window.api.getUserFavorites();

    if (properties.length === 0) {
        propertyGrid.innerHTML = '<p style="text-align:center; width:100%; color:var(--text-secondary);">No properties available matching your criteria.</p>';
        return;
    }

    properties.forEach((prop, index) => {
        const cardNode = document.createElement('div');
        cardNode.className = 'property-card glass-panel';
        cardNode.style.animation = `fadeInUp 0.6s ease ${index * 0.1}s forwards`;
        cardNode.style.opacity = '0';

        const isFav = favs.includes(prop.id) ? 'saved' : '';
        const iconName = favs.includes(prop.id) ? 'heart' : 'heart-outline';

        cardNode.innerHTML = `
            <div class="card-img-wrapper">
                <img src="${prop.imageUrl || 'https://images.unsplash.com/photo-1542314831-c53cd3816002'}" alt="${prop.title}" class="card-img">
                <button class="favorite-btn ${isFav}" onclick="toggleFav(event, this, ${prop.id})">
                    <ion-icon name="${iconName}"></ion-icon>
                </button>
            </div>
            <div class="card-content">
                <div class="card-header">
                    <h3 class="card-title">${prop.title}</h3>
                    <div class="card-rating">
                        <ion-icon name="star"></ion-icon>
                        ${prop.rating || 'New'}
                    </div>
                </div>
                <div class="card-location">
                    <ion-icon name="location-outline"></ion-icon>
                    ${prop.location} &bull; Capacity: ${prop.capacity}
                </div>
                <div class="card-footer">
                    <div class="price">$${prop.price}<span>/night</span></div>
                    <button class="btn-book" onclick="openBookingModal(${prop.id}, '${prop.title}')">Book Now</button>
                </div>
            </div>
        `;
        propertyGrid.appendChild(cardNode);
    });
}

// Search Logic
function setupSearchLogic() {
    if (!searchBtn) return;

    searchBtn.addEventListener('click', async () => {
        const inputs = document.querySelectorAll('.search-input input');
        // inputs[0] is Location
        const locationQuery = inputs[0].value;
        const checkIn = inputs[1].value;
        const checkOut = inputs[2].value;
        const guests = inputs[3].value;

        if (checkIn && checkOut && new Date(checkIn) >= new Date(checkOut)) {
            alert("Check-out date must be after check-in date.");
            return;
        }

        searchBtn.innerHTML = '<ion-icon name="sync"></ion-icon>'; // loading state

        try {
            const available = await window.api.searchAvailableRooms(locationQuery, checkIn, checkOut, guests);

            // Update active filter visually back to ALL when searching
            if (filterBtns.length > 0) {
                filterBtns.forEach(b => b.classList.remove('active'));
                filterBtns[0].classList.add('active');
            }

            renderProperties(available);
        } catch (error) {
            console.error(error);
            propertyGrid.innerHTML = '<p style="text-align:center; width:100%; color:red;">Search failed.</p>';
        } finally {
            searchBtn.innerHTML = '<ion-icon name="search"></ion-icon>';
        }
    });
}

// Category Filter Logic
function setupFilterLogic() {
    filterBtns.forEach(btn => {
        btn.addEventListener('click', async (e) => {
            filterBtns.forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');

            const filterType = e.target.getAttribute('data-type');

            try {
                const properties = await window.api.getRooms(); // get fresh list

                if (filterType === 'all') {
                    renderProperties(properties);
                } else {
                    // Case insensitive comparison
                    const filtered = properties.filter(p => p.type.toLowerCase() === filterType.toLowerCase());
                    renderProperties(filtered);
                }
            } catch (error) {
                console.error("Filter failed", error);
            }
        });
    });
}

// Create Modal System Dynamically
function openBookingModal(roomId, roomTitle) {
    if (!window.api.getCurrentUser()) {
        window.location.href = 'login.html';
        return;
    }

    // Pre-fill dates from search bar if available
    const inputs = document.querySelectorAll('.search-input input');
    const ciVal = inputs[1].value;
    const coVal = inputs[2].value;
    const gVal = inputs[3].value || 1;

    const modalHTML = `
        <div id="bookingModal" style="position:fixed; top:0; left:0; right:0; bottom:0; background:rgba(0,0,0,0.8); z-index:9999; display:flex; align-items:center; justify-content:center; backdrop-filter:blur(5px);">
            <div class="glass-panel" style="background:var(--bg-main); padding: 3rem; border-radius: 20px; width:100%; max-width:500px; position:relative;">
                <button onclick="document.getElementById('bookingModal').remove()" style="position:absolute; top:1rem; right:1rem; background:none; border:none; color:white; font-size:1.5rem; cursor:pointer;"><ion-icon name="close"></ion-icon></button>
                <h2 style="margin-bottom:0.5rem;">Book your stay</h2>
                <p style="color:var(--text-secondary); margin-bottom:2rem;">${roomTitle}</p>
                
                <div class="form-group" style="margin-bottom:1rem;">
                    <label>Check-in Date</label>
                    <input type="date" id="modalCi" value="${ciVal}" style="width:100%; padding:0.8rem; border-radius:10px; border:1px solid var(--glass-border); background:rgba(0,0,0,0.3); color:white; outline:none;" required>
                </div>
                <div class="form-group" style="margin-bottom:1rem;">
                    <label>Check-out Date</label>
                    <input type="date" id="modalCo" value="${coVal}" style="width:100%; padding:0.8rem; border-radius:10px; border:1px solid var(--glass-border); background:rgba(0,0,0,0.3); color:white; outline:none;" required>
                </div>
                <div class="form-group" style="margin-bottom:2rem;">
                    <label>Number of Guests</label>
                    <input type="number" id="modalGuests" value="${gVal}" min="1" style="width:100%; padding:0.8rem; border-radius:10px; border:1px solid var(--glass-border); background:rgba(0,0,0,0.3); color:white; outline:none;" required>
                </div>
                
                <div id="modalErr" style="color:#ef4444; margin-bottom:1rem; display:none;"></div>
                <button id="confirmBookBtn" class="btn-primary" style="width:100%; padding:1rem; font-size:1.1rem;">Confirm Booking</button>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalHTML);

    document.getElementById('confirmBookBtn').addEventListener('click', async (e) => {
        const ci = document.getElementById('modalCi').value;
        const co = document.getElementById('modalCo').value;
        const guests = document.getElementById('modalGuests').value;
        const errBox = document.getElementById('modalErr');
        const btn = e.target;

        if (!ci || !co || !guests) {
            errBox.textContent = "Please fill all fields.";
            errBox.style.display = "block";
            return;
        }
        if (new Date(ci) >= new Date(co)) {
            errBox.textContent = "Check-out must be after Check-in.";
            errBox.style.display = "block";
            return;
        }

        btn.innerHTML = "Processing...";
        btn.disabled = true;
        errBox.style.display = "none";

        try {
            await window.api.createBooking(roomId, ci, co, parseInt(guests));
            document.getElementById('bookingModal').innerHTML = `
                <div class="glass-panel" style="background:var(--bg-main); padding: 3rem; border-radius: 20px; width:100%; max-width:500px; text-align:center;">
                    <ion-icon name="checkmark-circle" style="font-size:5rem; color:#10b981; margin-bottom:1rem;"></ion-icon>
                    <h2>Booking Confirmed!</h2>
                    <p style="color:var(--text-secondary); margin-top:1rem; margin-bottom:2rem;">Your luxury stay has been secured.</p>
                    <button class="btn-primary" onclick="window.location.href='profile.html'">View in Profile</button>
                </div>
            `;
        } catch (error) {
            errBox.textContent = error.message;
            errBox.style.display = "block";
            btn.innerHTML = "Confirm Booking";
            btn.disabled = false;
        }
    });
}

// Favorite Interaction
async function toggleFav(e, btnEle, roomId) {
    e.stopPropagation(); // prevent card click
    try {
        const res = await window.api.toggleFavorite(roomId);
        if (res.favorited) {
            btnEle.classList.add('saved');
            btnEle.innerHTML = '<ion-icon name="heart"></ion-icon>';
        } else {
            btnEle.classList.remove('saved');
            btnEle.innerHTML = '<ion-icon name="heart-outline"></ion-icon>';
        }
    } catch (err) {
        alert("Please log in to save favorites.");
        window.location.href = 'login.html';
    }
}
