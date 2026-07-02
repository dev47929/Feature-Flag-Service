function FlagCard({ flag }) {
  const date = new Date(flag.createdAt).toLocaleDateString()

  return (
    <div className={`bg-white border border-gray-200 rounded-lg px-5 py-4 transition-shadow hover:shadow-sm ${flag.enabled ? 'border-l-4 border-l-green-500' : 'border-l-4 border-l-red-500'}`}>
      <div className="flex items-center justify-between mb-1.5">
        <h3 className="text-base font-semibold text-gray-900 m-0">{flag.name}</h3>
        <span className={`text-xs font-semibold px-2.5 py-0.5 rounded-full uppercase tracking-wider ${flag.enabled ? 'bg-green-50 text-green-600' : 'bg-red-50 text-red-600'}`}>
          {flag.enabled ? 'Enabled' : 'Disabled'}
        </span>
      </div>
      {flag.description && <p className="text-sm text-gray-600 mb-2.5">{flag.description}</p>}
      <div className="flex gap-2 items-center flex-wrap">
        <span className="text-xs px-2 py-0.5 rounded bg-gray-100 text-gray-600">{flag.environment}</span>
        {flag.rolloutPercentage != null && (
          <span className="text-xs px-2 py-0.5 rounded bg-blue-50 text-blue-700">{flag.rolloutPercentage}% rollout</span>
        )}
        <span className="text-xs text-gray-400 ml-auto">Created {date}</span>
      </div>
    </div>
  )
}

export default FlagCard
