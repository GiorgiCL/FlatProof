import { useEffect, useMemo, useState } from 'react'

const emptyRegisterForm = {
  name: '',
  email: '',
  password: '',
}

const emptyLoginForm = {
  email: '',
  password: '',
}

const emptyPropertyForm = {
  title: '',
  address: '',
  description: '',
}

const emptyReportForm = {
  notes: '',
}

const emptyRoomForm = {
  roomName: '',
}

const emptyItemForm = {
  itemType: '',
  conditionStatus: 'GOOD',
  comment: '',
}

function parseJwtSubject(token) {
  try {
    const payload = token.split('.')[1]
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/')
    const json = decodeURIComponent(
        atob(base64)
            .split('')
            .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
            .join('')
    )
    return JSON.parse(json).sub || 'Unknown user'
  } catch {
    return 'Unknown user'
  }
}

function App() {
  const [token, setToken] = useState(localStorage.getItem('flatproof_token') || '')
  const [authMode, setAuthMode] = useState('login')
  const [registerForm, setRegisterForm] = useState(emptyRegisterForm)
  const [loginForm, setLoginForm] = useState(emptyLoginForm)

  const [properties, setProperties] = useState([])
  const [propertyForm, setPropertyForm] = useState(emptyPropertyForm)
  const [selectedPropertyId, setSelectedPropertyId] = useState(null)

  const [reports, setReports] = useState([])
  const [reportForm, setReportForm] = useState(emptyReportForm)
  const [selectedReportId, setSelectedReportId] = useState(null)

  const [reportDetails, setReportDetails] = useState(null)
  const [reportEvidence, setReportEvidence] = useState([])
  const [roomForm, setRoomForm] = useState(emptyRoomForm)
  const [itemForms, setItemForms] = useState({})

  const [reportEvidenceFile, setReportEvidenceFile] = useState(null)
  const [itemEvidenceFiles, setItemEvidenceFiles] = useState({})

  const [hashResult, setHashResult] = useState(null)
  const [anchorResult, setAnchorResult] = useState(null)
  const [blockchainVerifyResult, setBlockchainVerifyResult] = useState(null)

  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const currentUserEmail = useMemo(() => {
    if (!token) return ''
    return parseJwtSubject(token)
  }, [token])

  function setSuccess(text) {
    setMessage(text)
    setError('')
    setTimeout(() => setMessage(''), 5000)
  }

  function setFailure(text) {
    setError(text)
    setMessage('')
    setTimeout(() => setError(''), 5000)
  }

  function clearFeedback() {
    setMessage('')
    setError('')
  }

  async function apiFetch(path, options = {}) {
    const headers = {
      ...(options.headers || {}),
    }

    if (token && !path.includes('/auth/')) {
      headers.Authorization = `Bearer ${token}`
    }

    if (!(options.body instanceof FormData)) {
      headers['Content-Type'] = 'application/json'
    }

    const response = await fetch(path, {
      ...options,
      headers,
    })

    const contentType = response.headers.get('content-type') || ''
    const isJson = contentType.includes('application/json')
    const data = isJson ? await response.json() : await response.text()

    if (!response.ok) {
      const errorMessage =
          typeof data === 'object' && data !== null
              ? data.message || data.error || JSON.stringify(data)
              : data || 'Request failed'
      throw new Error(errorMessage)
    }

    return data
  }

  async function loadProperties() {
    const data = await apiFetch('/api/properties')
    setProperties(data)
  }

  async function loadReports(propertyId) {
    const data = await apiFetch(`/api/properties/${propertyId}/reports`)
    setReports(data)
  }

  async function loadReportDetails(reportId) {
    const details = await apiFetch(`/api/reports/${reportId}/details`)
    setReportDetails(details)
  }

  async function loadReportEvidence(reportId) {
    const data = await apiFetch(`/api/reports/${reportId}/evidence`)
    setReportEvidence(data)
  }

  async function refreshSelectedProperty(propertyId) {
    await loadReports(propertyId)
  }

  async function refreshSelectedReport(reportId) {
    await loadReportDetails(reportId)
    await loadReportEvidence(reportId)
  }

  useEffect(() => {
    if (!token) return

        ;(async () => {
      try {
        clearFeedback()
        await loadProperties()
      } catch (err) {
        setFailure(err.message)
      }
    })()
  }, [token])

  async function handleRegister(e) {
    e.preventDefault()
    try {
      setLoading(true)
      clearFeedback()
      const data = await apiFetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registerForm),
      })
      localStorage.setItem('flatproof_token', data.token)
      setToken(data.token)
      setRegisterForm(emptyRegisterForm)
      setSuccess('Registration successful')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleLogin(e) {
    e.preventDefault()
    try {
      setLoading(true)
      clearFeedback()
      const data = await apiFetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginForm),
      })
      localStorage.setItem('flatproof_token', data.token)
      setToken(data.token)
      setLoginForm(emptyLoginForm)
      setSuccess('Login successful')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  function handleLogout() {
    localStorage.removeItem('flatproof_token')
    setToken('')
    setProperties([])
    setReports([])
    setReportDetails(null)
    setReportEvidence([])
    setSelectedPropertyId(null)
    setSelectedReportId(null)
    setHashResult(null)
    setAnchorResult(null)
    setBlockchainVerifyResult(null)
    clearFeedback()
  }

  async function handleCreateProperty(e) {
    e.preventDefault()
    try {
      setLoading(true)
      clearFeedback()
      await apiFetch('/api/properties', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(propertyForm),
      })
      setPropertyForm(emptyPropertyForm)
      await loadProperties()
      setSuccess('Property created successfully')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleSelectProperty(propertyId) {
    try {
      setSelectedPropertyId(propertyId)
      setSelectedReportId(null)
      setReportDetails(null)
      setReportEvidence([])
      setHashResult(null)
      setAnchorResult(null)
      setBlockchainVerifyResult(null)
      clearFeedback()
      await loadReports(propertyId)
    } catch (err) {
      setFailure(err.message)
    }
  }

  async function handleCreateReport(e) {
    e.preventDefault()
    if (!selectedPropertyId) {
      setFailure('Select a property first')
      return
    }

    try {
      setLoading(true)
      clearFeedback()
      await apiFetch(`/api/properties/${selectedPropertyId}/reports`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(reportForm),
      })
      setReportForm(emptyReportForm)
      await refreshSelectedProperty(selectedPropertyId)
      setSuccess('Report created successfully')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleSelectReport(reportId) {
    try {
      setSelectedReportId(reportId)
      setHashResult(null)
      setAnchorResult(null)
      setBlockchainVerifyResult(null)
      clearFeedback()
      await refreshSelectedReport(reportId)
    } catch (err) {
      setFailure(err.message)
    }
  }

  async function handleAddRoom(e) {
    e.preventDefault()
    if (!selectedReportId) {
      setFailure('Select a report first')
      return
    }

    try {
      setLoading(true)
      clearFeedback()
      await apiFetch(`/api/reports/${selectedReportId}/rooms`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(roomForm),
      })
      setRoomForm(emptyRoomForm)
      await refreshSelectedReport(selectedReportId)
      setSuccess('Room added successfully')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  function updateItemForm(roomId, field, value) {
    setItemForms((prev) => ({
      ...prev,
      [roomId]: {
        ...(prev[roomId] || emptyItemForm),
        [field]: value,
      },
    }))
  }

  async function handleAddItem(roomId) {
    const form = itemForms[roomId] || emptyItemForm

    try {
      setLoading(true)
      clearFeedback()
      await apiFetch(`/api/rooms/${roomId}/items`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form),
      })
      setItemForms((prev) => ({ ...prev, [roomId]: emptyItemForm }))
      await refreshSelectedReport(selectedReportId)
      setSuccess('Condition item added successfully')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleUploadReportEvidence() {
    if (!selectedReportId || !reportEvidenceFile) {
      setFailure('Select a report and choose a file first')
      return
    }

    try {
      setLoading(true)
      clearFeedback()
      const formData = new FormData()
      formData.append('file', reportEvidenceFile)

      await apiFetch(`/api/reports/${selectedReportId}/evidence`, {
        method: 'POST',
        body: formData,
      })

      setReportEvidenceFile(null)
      await loadReportEvidence(selectedReportId)
      setSuccess('Report evidence uploaded successfully')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  function setItemEvidenceFile(itemId, file) {
    setItemEvidenceFiles((prev) => ({
      ...prev,
      [itemId]: file,
    }))
  }

  async function handleUploadItemEvidence(itemId) {
    const file = itemEvidenceFiles[itemId]
    if (!file) {
      setFailure('Choose a file first')
      return
    }

    try {
      setLoading(true)
      clearFeedback()
      const formData = new FormData()
      formData.append('file', file)

      await apiFetch(`/api/items/${itemId}/evidence`, {
        method: 'POST',
        body: formData,
      })

      await refreshSelectedReport(selectedReportId)
      setSuccess('Item evidence uploaded successfully')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleFinalizeReport() {
    if (!selectedReportId) return

    try {
      setLoading(true)
      clearFeedback()
      await apiFetch(`/api/reports/${selectedReportId}/finalize`, {
        method: 'POST',
      })
      await refreshSelectedReport(selectedReportId)
      await refreshSelectedProperty(selectedPropertyId)
      setSuccess('Report finalized successfully')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleVerifyHash() {
    if (!selectedReportId) return

    try {
      setLoading(true)
      clearFeedback()
      const data = await apiFetch(`/api/reports/${selectedReportId}/verify-hash`)
      setHashResult(data)
      setSuccess('Hash verification completed')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleAnchorReport() {
    if (!selectedReportId) return

    try {
      setLoading(true)
      clearFeedback()
      const data = await apiFetch(`/api/reports/${selectedReportId}/anchor`, {
        method: 'POST',
      })
      setAnchorResult(data)
      await refreshSelectedProperty(selectedPropertyId)
      setSuccess('Report anchored on blockchain successfully')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleVerifyBlockchain() {
    if (!selectedReportId) return

    try {
      setLoading(true)
      clearFeedback()
      const data = await apiFetch(`/api/reports/${selectedReportId}/verify-blockchain`)
      setBlockchainVerifyResult(data)
      setSuccess('Blockchain verification completed')
    } catch (err) {
      setFailure(err.message)
    } finally {
      setLoading(false)
    }
  }

  const selectedProperty = properties.find((p) => p.id === selectedPropertyId)
  const selectedReport = reports.find((r) => r.id === selectedReportId)
  const reportIsFinalized = reportDetails?.status === 'FINALIZED'

  return (
      <div className="app-shell">
        <header className="topbar">
          <div>
            <h1>🏠 FlatProof</h1>
            <p>Tamper-evident property inspection system</p>
          </div>
          <div className="topbar-actions">
            {token ? (
                <>
                  <span className="pill">📧 {currentUserEmail}</span>
                  <button className="secondary-btn" onClick={handleLogout}>🚪 Logout</button>
                </>
            ) : (
                <span className="pill muted">🔒 Not authenticated</span>
            )}
          </div>
        </header>

        {message && <div className="banner success">✅ {message}</div>}
        {error && <div className="banner error">❌ {error}</div>}

        {!token ? (
            <section className="auth-wrapper">
              <div className="card auth-card">
                <div className="tabs">
                  <button
                      className={authMode === 'login' ? 'tab active' : 'tab'}
                      onClick={() => setAuthMode('login')}
                  >
                    🔐 Login
                  </button>
                  <button
                      className={authMode === 'register' ? 'tab active' : 'tab'}
                      onClick={() => setAuthMode('register')}
                  >
                    📝 Register
                  </button>
                </div>

                {authMode === 'login' ? (
                    <form className="form-grid" onSubmit={handleLogin}>
                      <input
                          type="email"
                          placeholder="Email"
                          value={loginForm.email}
                          onChange={(e) => setLoginForm({ ...loginForm, email: e.target.value })}
                          required
                      />
                      <input
                          type="password"
                          placeholder="Password"
                          value={loginForm.password}
                          onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
                          required
                      />
                      <button className="primary-btn" disabled={loading} type="submit">
                        {loading ? '⏳ Working...' : 'Login'}
                      </button>
                    </form>
                ) : (
                    <form className="form-grid" onSubmit={handleRegister}>
                      <input
                          type="text"
                          placeholder="Name"
                          value={registerForm.name}
                          onChange={(e) => setRegisterForm({ ...registerForm, name: e.target.value })}
                          required
                      />
                      <input
                          type="email"
                          placeholder="Email"
                          value={registerForm.email}
                          onChange={(e) => setRegisterForm({ ...registerForm, email: e.target.value })}
                          required
                      />
                      <input
                          type="password"
                          placeholder="Password"
                          value={registerForm.password}
                          onChange={(e) => setRegisterForm({ ...registerForm, password: e.target.value })}
                          required
                      />
                      <button className="primary-btn" disabled={loading} type="submit">
                        {loading ? '⏳ Working...' : 'Register'}
                      </button>
                    </form>
                )}
              </div>
            </section>
        ) : (
            <main className="dashboard-grid">
              <section className="card panel">
                <div className="panel-header">
                  <h2>🏘️ Properties</h2>
                </div>

                <form className="form-grid" onSubmit={handleCreateProperty}>
                  <input
                      type="text"
                      placeholder="Property Title"
                      value={propertyForm.title}
                      onChange={(e) => setPropertyForm({ ...propertyForm, title: e.target.value })}
                      required
                  />
                  <input
                      type="text"
                      placeholder="Address"
                      value={propertyForm.address}
                      onChange={(e) => setPropertyForm({ ...propertyForm, address: e.target.value })}
                      required
                  />
                  <textarea
                      placeholder="Description"
                      value={propertyForm.description}
                      onChange={(e) => setPropertyForm({ ...propertyForm, description: e.target.value })}
                  />
                  <button className="primary-btn" disabled={loading} type="submit">
                    + Create Property
                  </button>
                </form>

                <div className="list-stack">
                  {properties.length === 0 && (
                      <p className="placeholder-text">No properties yet. Create your first property above.</p>
                  )}
                  {properties.map((property) => (
                      <button
                          key={property.id}
                          className={selectedPropertyId === property.id ? 'list-item active' : 'list-item'}
                          onClick={() => handleSelectProperty(property.id)}
                      >
                        <strong>{property.title}</strong>
                        <span>{property.address}</span>
                      </button>
                  ))}
                </div>
              </section>

              <section className="card panel">
                <div className="panel-header">
                  <h2>📋 Reports</h2>
                  {selectedProperty && <span className="pill">{selectedProperty.title}</span>}
                </div>

                {selectedPropertyId ? (
                    <>
                      <form className="form-grid" onSubmit={handleCreateReport}>
                  <textarea
                      placeholder="Report notes (optional)"
                      value={reportForm.notes}
                      onChange={(e) => setReportForm({ ...reportForm, notes: e.target.value })}
                  />
                        <button className="primary-btn" disabled={loading} type="submit">
                          + Create Report
                        </button>
                      </form>

                      <div className="list-stack">
                        {reports.length === 0 && (
                            <p className="placeholder-text">No reports for this property yet.</p>
                        )}
                        {reports.map((report) => (
                            <button
                                key={report.id}
                                className={selectedReportId === report.id ? 'list-item active' : 'list-item'}
                                onClick={() => handleSelectReport(report.id)}
                            >
                              <strong>Report #{report.id}</strong>
                              <span>Status: {report.status}</span>
                              <small>{new Date(report.createdAt).toLocaleDateString()}</small>
                            </button>
                        ))}
                      </div>
                    </>
                ) : (
                    <p className="placeholder-text">Select a property to view or create reports.</p>
                )}
              </section>

              <section className="card panel details-panel">
                <div className="panel-header">
                  <h2>🔍 Report Details</h2>
                  {selectedReport && <span className="pill">Report #{selectedReport.id}</span>}
                </div>

                {!selectedReportId || !reportDetails ? (
                    <p className="placeholder-text">Select a report to view its structure and actions.</p>
                ) : (
                    <>
                      <div className="info-grid">
                        <div className="info-box">
                          <span className="label">Status</span>
                          <strong>{reportDetails.status}</strong>
                        </div>
                        <div className="info-box">
                          <span className="label">Property</span>
                          <strong>{reportDetails.propertyTitle}</strong>
                        </div>
                        <div className="info-box">
                          <span className="label">Created By</span>
                          <strong>{reportDetails.createdByEmail}</strong>
                        </div>
                        <div className="info-box">
                          <span className="label">Notes</span>
                          <strong>{reportDetails.notes || '—'}</strong>
                        </div>
                        {reportDetails.finalizedAt && (
                            <div className="info-box">
                              <span className="label">Finalized At</span>
                              <strong>{new Date(reportDetails.finalizedAt).toLocaleString()}</strong>
                            </div>
                        )}
                      </div>

                      <div className="actions-row">
                        <button
                            className="primary-btn"
                            disabled={loading || reportIsFinalized}
                            onClick={handleFinalizeReport}
                        >
                          {reportIsFinalized ? '✓ Finalized' : '🔒 Finalize Report'}
                        </button>
                        <button className="secondary-btn" disabled={loading} onClick={handleVerifyHash}>
                          🔍 Verify Hash
                        </button>
                        <button className="secondary-btn" disabled={loading || !reportIsFinalized} onClick={handleAnchorReport}>
                          ⛓️ Anchor Report
                        </button>
                        <button className="secondary-btn" disabled={loading} onClick={handleVerifyBlockchain}>
                          ✅ Verify Blockchain
                        </button>
                      </div>

                      <div className="result-grid">
                        {hashResult && (
                            <div className="result-card">
                              <h3>📊 Hash Verification Result</h3>
                              <pre>{JSON.stringify(hashResult, null, 2)}</pre>
                            </div>
                        )}

                        {anchorResult && (
                            <div className="result-card">
                              <h3>⛓️ Blockchain Anchor Result</h3>
                              <pre>{JSON.stringify(anchorResult, null, 2)}</pre>
                            </div>
                        )}

                        {blockchainVerifyResult && (
                            <div className="result-card">
                              <h3>🔗 Blockchain Verification</h3>
                              <pre>{JSON.stringify(blockchainVerifyResult, null, 2)}</pre>
                            </div>
                        )}
                      </div>

                      {!reportIsFinalized && (
                          <div className="nested-card">
                            <h3>➕ Add Room</h3>
                            <form className="form-inline" onSubmit={handleAddRoom}>
                              <input
                                  type="text"
                                  placeholder="Room name (e.g., Kitchen, Bedroom)"
                                  value={roomForm.roomName}
                                  onChange={(e) => setRoomForm({ roomName: e.target.value })}
                                  required
                              />
                              <button className="primary-btn" disabled={loading} type="submit">
                                Add Room
                              </button>
                            </form>
                          </div>
                      )}

                      <div className="nested-card">
                        <h3>📎 Report Evidence</h3>
                        {!reportIsFinalized && (
                            <div className="form-inline">
                              <input
                                  type="file"
                                  onChange={(e) => setReportEvidenceFile(e.target.files?.[0] || null)}
                              />
                              <button className="primary-btn" disabled={loading} onClick={handleUploadReportEvidence}>
                                Upload Evidence
                              </button>
                            </div>
                        )}
                        <div className="evidence-list">
                          {reportEvidence.length === 0 && (
                              <p className="placeholder-text">No evidence files uploaded yet.</p>
                          )}
                          {reportEvidence.map((file) => (
                              <div className="mini-card" key={file.id}>
                                <strong>📄 {file.originalFileName}</strong>
                                <small>Hash: {file.fileHash}</small>
                              </div>
                          ))}
                        </div>
                      </div>

                      <div className="rooms-wrapper">
                        {reportDetails.rooms?.map((room) => (
                            <div className="room-card" key={room.id}>
                              <div className="room-header">
                                <h3>🚪 {room.roomName}</h3>
                                <span className="pill">Room #{room.id}</span>
                              </div>

                              {!reportIsFinalized && (
                                  <div className="item-form-grid">
                                    <input
                                        type="text"
                                        placeholder="Item type (e.g., Wall, Floor, Window)"
                                        value={(itemForms[room.id] || emptyItemForm).itemType}
                                        onChange={(e) => updateItemForm(room.id, 'itemType', e.target.value)}
                                    />
                                    <select
                                        value={(itemForms[room.id] || emptyItemForm).conditionStatus}
                                        onChange={(e) => updateItemForm(room.id, 'conditionStatus', e.target.value)}
                                    >
                                      <option value="GOOD">✅ GOOD</option>
                                      <option value="DAMAGED">⚠️ DAMAGED</option>
                                      <option value="MISSING">❌ MISSING</option>
                                      <option value="DIRTY">🧹 DIRTY</option>
                                    </select>
                                    <input
                                        type="text"
                                        placeholder="Comment"
                                        value={(itemForms[room.id] || emptyItemForm).comment}
                                        onChange={(e) => updateItemForm(room.id, 'comment', e.target.value)}
                                    />
                                    <button className="primary-btn" disabled={loading} onClick={() => handleAddItem(room.id)}>
                                      + Add Item
                                    </button>
                                  </div>
                              )}

                              <div className="items-list">
                                {room.items?.length === 0 && (
                                    <p className="placeholder-text">No condition items added yet.</p>
                                )}
                                {room.items?.map((item) => (
                                    <div className="item-card" key={item.id}>
                                      <div>
                                        <strong>{item.itemType}</strong>
                                        <p>Status: {item.conditionStatus}</p>
                                        {item.comment && <p>💬 {item.comment}</p>}
                                      </div>
                                      {!reportIsFinalized && (
                                          <div className="item-upload-box">
                                            <input
                                                type="file"
                                                onChange={(e) => setItemEvidenceFile(item.id, e.target.files?.[0] || null)}
                                            />
                                            <button className="secondary-btn" disabled={loading} onClick={() => handleUploadItemEvidence(item.id)}>
                                              Upload Evidence
                                            </button>
                                          </div>
                                      )}
                                    </div>
                                ))}
                              </div>
                            </div>
                        ))}
                      </div>
                    </>
                )}
              </section>
            </main>
        )}
      </div>
  )
}

export default App