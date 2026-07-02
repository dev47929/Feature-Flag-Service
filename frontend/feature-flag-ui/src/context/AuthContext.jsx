/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('ff_token'))
  const [userId, setUserId] = useState(() => localStorage.getItem('ff_userId'))
  const [demoMode, setDemoMode] = useState(false)

  useEffect(() => {
    if (token) localStorage.setItem('ff_token', token)
    else localStorage.removeItem('ff_token')
  }, [token])

  useEffect(() => {
    if (userId) localStorage.setItem('ff_userId', userId)
    else localStorage.removeItem('ff_userId')
  }, [userId])

  const login = (newToken, newUserId) => {
    setToken(newToken)
    setUserId(String(newUserId))
  }

  const logout = () => {
    setToken(null)
    setUserId(null)
    setDemoMode(false)
  }

  const enterDemoMode = () => setDemoMode(true)
  const exitDemoMode = () => setDemoMode(false)

  return (
    <AuthContext.Provider value={{
      token,
      userId,
      isAuthenticated: !!token || demoMode,
      demoMode,
      login,
      logout,
      enterDemoMode,
      exitDemoMode,
    }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
