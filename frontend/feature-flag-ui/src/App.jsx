import { useState, useEffect } from 'react'
import { useAuth } from './context/AuthContext'
import FilterBar from './components/FilterBar'
import FlagList from './components/FlagList'
import LoginPage from './components/LoginPage'
import SignupPage from './components/SignupPage'

const DEMO_FLAGS = [
  { id: 1, name: 'dark-mode', description: 'Enable dark mode across the entire app', enabled: true, rolloutPercentage: 100, environment: 'production', createdAt: '2025-11-01T10:00:00Z' },
  { id: 2, name: 'new-checkout-flow', description: 'Re-designed checkout with one-click purchase', enabled: true, rolloutPercentage: 25, environment: 'production', createdAt: '2026-02-15T14:30:00Z' },
  { id: 3, name: 'ai-recommendations', description: 'Personalized AI-powered product recommendations', enabled: false, rolloutPercentage: null, environment: 'production', createdAt: '2026-03-10T09:00:00Z' },
  { id: 4, name: 'beta-search', description: 'Experimental search with typo tolerance and facets', enabled: true, rolloutPercentage: 10, environment: 'staging', createdAt: '2026-04-01T16:00:00Z' },
  { id: 5, name: 'subscription-tiers', description: 'New subscription plans with premium features', enabled: false, rolloutPercentage: null, environment: 'staging', createdAt: '2026-05-20T11:00:00Z' },
  { id: 6, name: 'optimistic-caching', description: 'Client-side optimistic cache for faster page loads', enabled: true, rolloutPercentage: 50, environment: 'production', createdAt: '2026-06-01T08:00:00Z' },
  { id: 7, name: 'social-login', description: 'Google and GitHub OAuth login buttons', enabled: true, rolloutPercentage: 100, environment: 'development', createdAt: '2026-03-05T12:00:00Z' },
  { id: 8, name: 'two-factor-auth', description: '2FA via authenticator app or SMS', enabled: false, rolloutPercentage: null, environment: 'development', createdAt: '2026-04-10T15:00:00Z' },
  { id: 9, name: 'webhook-v2', description: 'Enhanced webhook system with retry and logging', enabled: true, rolloutPercentage: 100, environment: 'production', createdAt: '2025-12-01T07:00:00Z' },
]

function FlagDashboard({ demoMode }) {
  const { logout } = useAuth()
  const [flags, setFlags] = useState(demoMode ? DEMO_FLAGS : [])
  const [loading, setLoading] = useState(!demoMode)
  const [error, setError] = useState(null)
  const [filters, setFilters] = useState({
    name: '',
    environment: 'all',
    status: 'all',
  })

  useEffect(() => {
    if (demoMode) return
    fetch('/flags')
      .then(r => {
        if (!r.ok) throw new Error(`HTTP ${r.status}`)
        return r.json()
      })
      .then(data => { setFlags(data); setLoading(false) })
      .catch(err => { setError(err.message); setLoading(false) })
  }, [demoMode])

  const environments = [...new Set(flags.map(f => f.environment))]

  const filtered = flags.filter(f => {
    if (filters.name && !f.name.toLowerCase().includes(filters.name.toLowerCase())) return false
    if (filters.environment !== 'all' && f.environment !== filters.environment) return false
    if (filters.status === 'enabled' && !f.enabled) return false
    if (filters.status === 'disabled' && f.enabled) return false
    return true
  })

  return (
    <div className="max-w-4xl mx-auto">
      {demoMode && (
        <div className="flex items-center gap-2 mb-4 px-4 py-2 bg-amber-50 border border-amber-200 rounded-lg text-sm text-amber-800">
          <span className="text-lg">&#128640;</span>
          <span className="flex-1">You are viewing the demo with sample data. <button onClick={logout} className="underline hover:no-underline cursor-pointer bg-transparent border-none p-0 text-amber-800 font-inherit">Sign in</button> to use the real app.</span>
        </div>
      )}
      <header className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Feature Flags</h1>
          <p className="text-sm text-gray-500 mt-1">{flags.length} total flags</p>
        </div>
        <button
          onClick={logout}
          className="text-sm px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-100 cursor-pointer"
        >
          {demoMode ? 'Exit Demo' : 'Sign out'}
        </button>
      </header>
      <FilterBar
        filters={filters}
        onFilterChange={setFilters}
        environments={environments}
      />
      {loading && <p className="text-center py-10 text-gray-500 text-sm">Loading...</p>}
      {error && <p className="text-center py-10 text-red-500 text-sm">Error: {error}</p>}
      {!loading && !error && <FlagList flags={filtered} />}
    </div>
  )
}

function App() {
  const { isAuthenticated, demoMode } = useAuth()
  const [page, setPage] = useState('login')

  if (!isAuthenticated) {
    if (page === 'signup') return <SignupPage onSwitch={() => setPage('login')} />
    return <LoginPage onSwitch={() => setPage('signup')} />
  }

  return <FlagDashboard demoMode={demoMode} />
}

export default App
