'use client';

import { useState } from 'react';

export default function Home() {
  const [message, setMessage] = useState('');
  const analyzeMessage = async () => {
    const responce = await
      fetch('http://localhost:8080/api/analyze', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json', },
          body: JSON.stringify({
            message: message,
        }),
      });
    
    const data = await responce.json();
    console.log(data);
  };

  return(
    <main className='min-h-screen bg-gray-950 text-white'>

      <div className='mx-auto max-w-4xl px-6 py-16'>
        <h1 className='text-4xl font-bold'>
          De-scam
        </h1>
        <p className='mt-2 text-gray-400'>
          Analyze suspicious messages before you act...
        </p>

        <div className='mt-10'>
          <label
            htmlFor='message' className='mb-2 block text-sm font-medium'
          >
            Message:
          </label>
          <textarea id='message' placeholder='Paste/Enter the message to analyze'
            value={message}
            onChange={(event) =>
              setMessage(event.target.value)}
            className='h-48 w-full resize-none rounded-lg border border-gray bg-gray-900 p-4 text-white outline-none focus:border-gray-500'
          />
          <button type='button' onClick={analyzeMessage}
            className='mt-4 rounded-lg bg-white px-6 py-3 font-semibold text-black hover:bg-gray-200'
          >
            Analyze
          </button>
        </div>
      </div>
    </main>
  );
}