// ==========================================
// Mock API & LocalStorage Database Simulator
// ==========================================

// --- Initial Mock Data ---
const initialRooms = [
    { id: 1, title: "Sapphire Horizon Resort", type: "Resort", location: "Santorini, Greece", price: 450, rating: 4.9, capacity: 2, imageUrl: "https://images.unsplash.com/photo-1590523251334-a26cb9bc4ce2?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
    { id: 2, title: "Alpine Glass Villa", type: "Villa", location: "Zermatt, Switzerland", price: 890, rating: 5.0, capacity: 6, imageUrl: "https://images.unsplash.com/photo-1510798831971-661eb04b3739?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
    { id: 3, title: "Metropolis Sky Loft", type: "Apartment", location: "New York, USA", price: 320, rating: 4.7, capacity: 4, imageUrl: "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
    { id: 4, title: "Azure Beachfront Villa", type: "Villa", location: "Maldives", price: 1250, rating: 5.0, capacity: 8, imageUrl: "https://images.unsplash.com/photo-1499793983690-e29da59ef1c2?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
    { id: 5, title: "Eiffel View Penthouse", type: "Apartment", location: "Paris, France", price: 600, rating: 4.8, capacity: 2, imageUrl: "https://images.unsplash.com/photo-1502602881462-2ba45a7bce5a?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
    { id: 6, title: "Sakura Garden Ryokan", type: "Resort", location: "Tokyo, Japan", price: 550, rating: 4.9, capacity: 4, imageUrl: "https://images.unsplash.com/photo-1542051812871-34f2cbdefd0f?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
    { id: 7, title: "Tuscan Vineyard Estate", type: "Villa", location: "Tuscany, Italy", price: 950, rating: 5.0, capacity: 10, imageUrl: "https://images.unsplash.com/photo-1523531294919-4bcd7c65e216?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
    { id: 8, title: "Aegean Cliff House", type: "Villa", location: "Mykonos, Greece", price: 1100, rating: 4.9, capacity: 6, imageUrl: "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" }
];

const initialUsers = [
    { id: 1, firstName: "Admin", lastName: "User", email: "admin@luxestays.com", password: "password123", role: "ADMIN" },
    { id: 2, firstName: "John", lastName: "Doe", email: "john@example.com", password: "password123", role: "CUSTOMER" }
];

// --- Database Initialization ---
function initDB() {
    let currentRooms = JSON.parse(localStorage.getItem('luxestays_rooms'));
    if (!currentRooms || currentRooms.length < initialRooms.length) {
        localStorage.setItem('luxestays_rooms', JSON.stringify(initialRooms));
    }
    if (!localStorage.getItem('luxestays_users')) {
        localStorage.setItem('luxestays_users', JSON.stringify(initialUsers));
    }
    if (!localStorage.getItem('luxestays_bookings')) {
        localStorage.setItem('luxestays_bookings', JSON.stringify([]));
    }
    if (!localStorage.getItem('luxestays_favorites')) {
        localStorage.setItem('luxestays_favorites', JSON.stringify([]));
    }
}
initDB();

// --- Utility: Simulate Network Delay ---
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

// --- The API Interceptor ---
// We are connecting Auth and Rooms to the REAL backend, but keeping Bookings and Favorites mocked!
const API_BASE = 'http://localhost:8080/api';

window.api = {
    // ---- AUTHENTICATION (REAL BACKEND) ----
    async login(email, password) {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        if (!response.ok) {
            throw new Error("Invalid credentials or server error.");
        }
        const token = await response.text();
        localStorage.setItem('jwtToken', token);
        return { token };
    },

    async register(userCreationDTO) {
        const response = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ ...userCreationDTO, role: userCreationDTO.role || 'Customer' })
        });
        if (!response.ok) {
            throw new Error("Email already in use or configuration error.");
        }
        return "Successfully registered!";
    },

    logout() {
        localStorage.removeItem('jwtToken');
        window.location.href = 'index.html';
    },

    // Helper: build Authorization header with JWT token
    getAuthHeaders() {
        const token = localStorage.getItem('jwtToken');
        return {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {})
        };
    },

    // Get current logged-in user from fake JWT
    getCurrentUser() {
        const token = localStorage.getItem('jwtToken');
        if (!token) return null;
        try {
            const payloadStr = atob(token.split('.')[1]);
            return JSON.parse(payloadStr); // { sub: email, role: role, id: id, name: firstName }
        } catch (e) {
            return null;
        }
    },

    // Check if route requires admin
    requireAdmin() {
        const user = this.getCurrentUser();
        if (!user || user.role !== 'ADMIN') {
            alert("Unauthorized: Admin access required");
            window.location.href = 'index.html';
            return false;
        }
        return true;
    },

    // Check if user is logged in
    requireAuth() {
        if (!this.getCurrentUser()) {
            window.location.href = 'login.html';
            return false;
        }
        return true;
    },

    // ---- ROOMS ----
    _mapToFrontendRoom(backendRoom) {
        // Bridges backend Room.java DTO to frontend's expected properties
        return {
            id: backendRoom.id,
            title: backendRoom.roomNumber,
            type: backendRoom.type,
            location: backendRoom.features || "International",
            price: backendRoom.pricePerNight,
            rating: 5.0, // Mocked rating since backend doesn't store one
            capacity: backendRoom.capacity,
            imageUrl: "https://images.unsplash.com/photo-1590523251334-a26cb9bc4ce2?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
        };
    },

    _mapToBackendRoom(frontendRoom) {
        // Bridges frontend modifications back to Room.java expectations
        return {
            roomNumber: frontendRoom.title,
            type: frontendRoom.type,
            capacity: parseInt(frontendRoom.capacity),
            pricePerNight: parseFloat(frontendRoom.price),
            features: "Updated Location"
        };
    },

    async getRooms() {
        const response = await fetch(`${API_BASE}/rooms`);
        const data = await response.json();
        return data.map(this._mapToFrontendRoom);
    },

    async getRoomById(id) {
        const response = await fetch(`${API_BASE}/rooms/${id}`);
        const data = await response.json();
        return this._mapToFrontendRoom(data);
    },

    async createRoom(roomData) {
        const params = this._mapToBackendRoom(roomData);
        const response = await fetch(`${API_BASE}/rooms`, {
            method: 'POST',
            headers: this.getAuthHeaders(),
            body: JSON.stringify(params)
        });
        if (!response.ok) throw new Error("Failed to create room.");
        return true;
    },

    async updateRoom(id, updatedData) {
        const params = this._mapToBackendRoom(updatedData);
        const response = await fetch(`${API_BASE}/rooms/${id}`, {
            method: 'PUT',
            headers: this.getAuthHeaders(),
            body: JSON.stringify(params)
        });
        if (!response.ok) throw new Error("Failed to update room.");
        return true;
    },

    async deleteRoom(id) {
        const response = await fetch(`${API_BASE}/rooms/${id}`, {
            method: 'DELETE',
            headers: this.getAuthHeaders()
        });
        if (!response.ok) throw new Error("Failed to delete room.");
        return true;
    },

    // ---- BOOKINGS ----
    async createBooking(roomId, checkIn, checkOut, guests) {
        await delay(600);
        const user = this.getCurrentUser();
        if (!user) throw new Error("Must be logged in to book");

        const bookings = JSON.parse(localStorage.getItem('luxestays_bookings'));

        // Check availability
        const isConflict = bookings.some(b =>
            b.roomId == roomId &&
            b.status !== 'CANCELLED' &&
            ((checkIn >= b.checkIn && checkIn < b.checkOut) ||
                (checkOut > b.checkIn && checkOut <= b.checkOut) ||
                (checkIn <= b.checkIn && checkOut >= b.checkOut))
        );

        if (isConflict) throw new Error("Room is already booked for these dates");

        const newBooking = {
            id: Date.now(),
            userId: user.id,
            roomId: roomId,
            checkIn: checkIn,
            checkOut: checkOut,
            guests: guests,
            status: "CONFIRMED",
            bookingDate: new Date().toISOString()
        };

        bookings.push(newBooking);
        localStorage.setItem('luxestays_bookings', JSON.stringify(bookings));
        return newBooking;
    },

    async getUserBookings() {
        await delay(400);
        const user = this.getCurrentUser();
        if (!user) throw new Error("Unauthorized");
        const bookings = JSON.parse(localStorage.getItem('luxestays_bookings'));
        return bookings.filter(b => b.userId === user.id);
    },

    async cancelBooking(bookingId) {
        await delay(300);
        const bookings = JSON.parse(localStorage.getItem('luxestays_bookings'));
        const index = bookings.findIndex(b => b.id == bookingId);
        if (index !== -1) {
            bookings[index].status = "CANCELLED";
            localStorage.setItem('luxestays_bookings', JSON.stringify(bookings));
            return true;
        }
        throw new Error("Booking not found");
    },

    // Search available rooms (Requires Booking system to know what's available!)
    async searchAvailableRooms(locationQuery, checkIn, checkOut, guests) {
        await delay(500);
        // We will fetch all the rooms from the true backend...
        let rooms = [];
        try {
            rooms = await this.getRooms();
        } catch (e) {
            rooms = JSON.parse(localStorage.getItem('luxestays_rooms'));
        }

        const bookings = JSON.parse(localStorage.getItem('luxestays_bookings'));

        let available = rooms;

        // Filter by location
        if (locationQuery && locationQuery.trim() !== '') {
            const query = locationQuery.toLowerCase().trim();
            available = available.filter(r => r.location.toLowerCase().includes(query) || r.title.toLowerCase().includes(query));
        }

        // Filter by capacity first
        if (guests) {
            available = available.filter(r => r.capacity >= parseInt(guests));
        }

        // Filter by date conflicts
        if (checkIn && checkOut) {
            const checkInDate = new Date(checkIn);
            const checkOutDate = new Date(checkOut);

            available = available.filter(room => {
                const roomBookings = bookings.filter(b => b.roomId == room.id && b.status !== 'CANCELLED');
                const hasConflict = roomBookings.some(b => {
                    const bIn = new Date(b.checkIn);
                    const bOut = new Date(b.checkOut);
                    return ((checkInDate >= bIn && checkInDate < bOut) ||
                        (checkOutDate > bIn && checkOutDate <= bOut) ||
                        (checkInDate <= bIn && checkOutDate >= bOut));
                });
                return !hasConflict;
            });
        }
        return available;
    },

    // ---- FAVORITES ----
    async toggleFavorite(roomId) {
        await delay(200);
        const user = this.getCurrentUser();
        if (!user) throw new Error("Must be logged in to save favorites");

        let favorites = JSON.parse(localStorage.getItem('luxestays_favorites') || '[]');

        // Find if this specific user already favorited this room
        const existingIndex = favorites.findIndex(f => f.userId === user.id && f.roomId === roomId);

        if (existingIndex !== -1) {
            // Remove it
            favorites.splice(existingIndex, 1);
            localStorage.setItem('luxestays_favorites', JSON.stringify(favorites));
            return { favorited: false };
        } else {
            // Add it
            favorites.push({ id: Date.now(), userId: user.id, roomId: roomId });
            localStorage.setItem('luxestays_favorites', JSON.stringify(favorites));
            return { favorited: true };
        }
    },

    async getUserFavorites() {
        await delay(300);
        const user = this.getCurrentUser();
        if (!user) return [];

        const favorites = JSON.parse(localStorage.getItem('luxestays_favorites') || '[]');
        return favorites.filter(f => f.userId === user.id).map(f => f.roomId);
    }
};

// --- Navbar Sync Logic ---
function updateNavbar() {
    const user = window.api.getCurrentUser();
    const authButtons = document.querySelector('.auth-buttons');
    if (!authButtons) return;

    if (user) {
        let adminLink = user.role === 'ADMIN' ? `<a href="admin.html" class="admin-link"><ion-icon name="shield-checkmark"></ion-icon> Admin</a>` : '';
        authButtons.innerHTML = `
            <div class="user-greeting" style="display:flex; align-items:center; gap:1.5rem;">
                ${adminLink}
                <a href="profile.html" style="color:var(--text-primary); text-decoration:none; font-weight:500; display:flex; align-items:center; gap:0.5rem;">
                    <ion-icon name="person-circle" style="font-size:1.5rem; color:var(--accent-primary)"></ion-icon>
                    ${user.name}
                </a>
                <button onclick="window.api.logout()" class="btn-ghost" style="padding:0.5rem 1rem;">Log Out</button>
            </div>
        `;
    } else {
        authButtons.innerHTML = `
            <button class="btn-ghost" onclick="window.location.href='login.html'">Log In</button>
            <button class="btn-primary" onclick="window.location.href='login.html'">Sign Up</button>
        `;
    }
}

// Call on load if navbar exists
document.addEventListener('DOMContentLoaded', updateNavbar);
