import { useState } from 'react';
import { FaThumbsUp, FaHeart, FaLaughSquint } from 'react-icons/fa';

function ReactionButton() {
    const [showReactions, setShowReactions] = useState(false);
    const [selectedReaction, setSelectedReaction] = useState(null);

    const reactions = [
        { name: 'Like', icon: <FaThumbsUp color="blue" /> },
        { name: 'Love', icon: <FaHeart color="red" /> },
        { name: 'Haha', icon: <FaLaughSquint color="orange" /> }
    ];

    return (
        <div
            style={{ position: 'relative', display: 'inline-block' }}
            onMouseEnter={() => setShowReactions(true)}
            onMouseLeave={() => setShowReactions(false)}
        >
            <button style={{ padding: '10px', cursor: 'pointer' }}>
                {selectedReaction ? (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '5px' }}>
                        {selectedReaction.icon}
                        <p style={{ margin: 0 }}>{selectedReaction.name}</p>
                    </div>
                ) : (
                    <div className="flex items-center gap-1 hover:text-blue-600">
                        👍 <span className="text-l font-bold drop-shadow-sm">Thích</span>
                    </div>
                )}
            </button>

            {showReactions && (
                <div
                    style={{
                        position: 'absolute',
                        top: '-30px',
                        background: '#fff',
                        border: '1px solid #ccc',
                        borderRadius: '20px',
                        padding: '5px 10px',
                        display: 'flex',
                        gap: '10px',
                        boxShadow: '0 2px 8px rgba(0,0,0,0.15)',
                        zIndex: 10,
                    }}
                >
                    {reactions.map((r) => (
                        <div
                            key={r.name}
                            style={{ cursor: 'pointer', fontSize: '25px' }}
                            onClick={() => setSelectedReaction(r)}
                            title={r.name}
                        >
                            {r.icon}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default ReactionButton;
