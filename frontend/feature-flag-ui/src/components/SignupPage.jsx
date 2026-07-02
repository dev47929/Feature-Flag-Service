import { useState } from 'react'
import { useAuth } from '../context/AuthContext'

function SignupPage({ onSwitch }) {
  const { login, enterDemoMode } = useAuth()
  const [form, setForm] = useState({ username: '', password: '', name: '', email: '' })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const update = (key, value) => setForm(prev => ({ ...prev, [key]: value }))

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      const res = await fetch('/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form),
      })
      if (!res.ok) {
        const text = await res.text()
        throw new Error(text || `HTTP ${res.status}`)
      }
      const data = await res.json()
      login(data.token, data.userId)
    } catch (err) {
      setError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <form onSubmit={handleSubmit} className="bg-white p-8 rounded-xl shadow-sm border border-gray-200 w-full max-w-sm">
        <h1 className="text-2xl font-bold text-gray-900 mb-1">Create account</h1>
        <p className="text-sm text-gray-500 mb-6">Start managing your feature flags</p>

        {error && <p className="text-sm text-red-600 bg-red-50 rounded-lg px-3 py-2 mb-4">{error}</p>}

        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
          <input type="text" value={form.username} onChange={e => update('username', e.target.value)} required
            className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white text-gray-800 focus:outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-500/15" />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
          <input type="text" value={form.name} onChange={e => update('name', e.target.value)} required
            className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white text-gray-800 focus:outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-500/15" />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input type="email" value={form.email} onChange={e => update('email', e.target.value)} required
            className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white text-gray-800 focus:outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-500/15" />
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
          <input type="password" value={form.password} onChange={e => update('password', e.target.value)} required
            className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white text-gray-800 focus:outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-500/15" />
        </div>

        <button type="submit" disabled={submitting}
          className="w-full py-2 px-4 text-sm font-semibold text-white bg-blue-600 rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer"
        >
          {submitting ? 'Creating account...' : 'Create account'}
        </button>

        <p className="text-sm text-gray-500 text-center mt-4">
          Already have an account?{' '}
          <button type="button" onClick={onSwitch} className="text-blue-600 hover:underline cursor-pointer bg-transparent border-none p-0 font-inherit">
            Sign in
          </button>
        </p>

        <div className="relative my-6">
          <div className="absolute inset-0 flex items-center"><div className="w-full border-t border-gray-200" /></div>
          <div className="relative flex justify-center"><span className="bg-white px-3 text-xs text-gray-400">or</span></div>
        </div>

        <button
          type="button"
          onClick={enterDemoMode}
          className="w-full py-2 px-4 text-sm font-semibold text-amber-700 bg-amber-50 border border-amber-200 rounded-lg hover:bg-amber-100 cursor-pointer"
        >
          Demo Mode
        </button>
      </form>
    </div>
  )
}

export default SignupPage
