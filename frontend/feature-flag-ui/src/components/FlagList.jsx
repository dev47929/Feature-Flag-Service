import FlagCard from './FlagCard'

function FlagList({ flags }) {
  if (flags.length === 0) {
    return <p className="text-center py-10 text-gray-500 text-sm">No flags match your filters.</p>
  }

  return (
    <div className="flex flex-col gap-3">
      {flags.map(flag => (
        <FlagCard key={flag.id} flag={flag} />
      ))}
    </div>
  )
}

export default FlagList
