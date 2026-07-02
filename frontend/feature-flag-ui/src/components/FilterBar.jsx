function FilterBar({ filters, onFilterChange, environments }) {
  const update = (key, value) => onFilterChange(prev => ({ ...prev, [key]: value }))

  return (
    <div className="flex gap-3 mb-6 flex-wrap">
      <input
        type="text"
        placeholder="Search by name..."
        value={filters.name}
        onChange={e => update('name', e.target.value)}
        className="flex-1 min-w-[200px] px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white text-gray-800 focus:outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-500/15"
      />
      <select
        value={filters.environment}
        onChange={e => update('environment', e.target.value)}
        className="px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white text-gray-800 focus:outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-500/15"
      >
        <option value="all">All Environments</option>
        {environments.map(env => (
          <option key={env} value={env}>{env}</option>
        ))}
      </select>
      <select
        value={filters.status}
        onChange={e => update('status', e.target.value)}
        className="px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white text-gray-800 focus:outline-none focus:border-blue-500 focus:ring-3 focus:ring-blue-500/15"
      >
        <option value="all">All Status</option>
        <option value="enabled">Enabled</option>
        <option value="disabled">Disabled</option>
      </select>
    </div>
  )
}

export default FilterBar
